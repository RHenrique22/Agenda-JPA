/** 
 * IFPB - TSI - PERSISTENCIA DE OBJETOS
 * @author Prof Fausto Ayres
 * 
 * 
 * ver configuracoes do DAO no arquivo dados.properties
 */


package dao;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class DAO<T> implements DAOInterface<T> {
	protected static EntityManager manager;
	protected static EntityManagerFactory factory;

	//usado pelo hibernate para adicionar mensagens no log.txt
	protected static final Log log = LogFactory.getLog(DAO.class);


	public DAO(){}

	public static void open(){
		if(manager==null) {	//padrão de projeto Singleton
			/*****************************************************************************
			 * 	Determinar o nome da unidade de persistencia a ser processada no persistence.xml
			 *  Este nome é a concatenacao dos nomes provedor + sgbd, lidos do arquivo dados.properties
			 *****************************************************************************/
			String nomeUnidadePersistencia="";
			String provedor="";
			String sgbd="";
			String banco="";
			String ip="";
			Properties dados = new Properties();
			try {
				dados.load(DAO.class.getResourceAsStream("/dados.properties"));
			}
			catch (Exception e) {
				log.info("DAO - open() - problema no arqquivo dados.properties: "+ e.getMessage());
				System.exit(0);
			} 
			provedor = dados.getProperty("provedor");
			sgbd = dados.getProperty("sgbd");
			ip = dados.getProperty("ip");
			banco = dados.getProperty("banco");
			nomeUnidadePersistencia = provedor +"-"+ sgbd;
			log.info("processando a unidade de persistencia: "+ nomeUnidadePersistencia);

			/**
			 *  	Alterar algumas propriedades do arquivo persistence.xml
			 */
			// alterar URL de conexão
			Properties propriedadesXMLalteradas = new Properties();
			if(sgbd.equals("postgres"))
				propriedadesXMLalteradas.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://"+ip+":5432/"+banco);
			else
				if(sgbd.equals("mysql"))
					propriedadesXMLalteradas.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://"+ip+":3306/"+banco+"?createDatabaseIfNotExist=true");

			factory = Persistence.createEntityManagerFactory(nomeUnidadePersistencia, propriedadesXMLalteradas);
			manager = factory.createEntityManager();	
		}
	}

	public static void close(){
		if (manager != null && manager.isOpen()) {
			manager.close();
			factory.close();
			manager=null;
		}
	}
	public void create(T obj){
		manager.persist(obj);
	}

	public abstract T read(Object chave);

	public T update(T obj){
		return manager.merge(obj);
	}
	public void delete(T obj) {
		manager.remove(obj);
	}


	@SuppressWarnings("unchecked")
	public List<T> readAll(){
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		TypedQuery<T> query = manager.createQuery("select x from " + type.getSimpleName() + " x",type);
		return  query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> readAllPagination(int firstResult, int maxResults) {
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		return manager.createQuery("select x from " + type.getSimpleName() + " x",type)
				.setFirstResult(firstResult - 1)
				.setMaxResults(maxResults)
				.getResultList();
	}

	public void deleteAll(){
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		String classe = type.getSimpleName();
		Query query = manager.createQuery("delete from " + classe);
		query.executeUpdate();
	}

	public static Connection getConnection() {
		try {
			String driver = (String) manager.getProperties().get("javax.persistence.jdbc.driver");
			String url = (String)	manager.getProperties().get("javax.persistence.jdbc.url");
			String user = (String)	manager.getProperties().get("javax.persistence.jdbc.user");
			String pass = (String)	manager.getProperties().get("javax.persistence.jdbc.password");
			Class.forName(driver);
			return DriverManager.getConnection(url, user, pass);
		} 
		catch (Exception ex) {
			return null;
		}
	}

	//----------------------- TRANSAÇÃO   ----------------------
	public static void begin(){
		if(!manager.getTransaction().isActive())
			manager.getTransaction().begin();
	}
	public static void commit(){
		if(manager.getTransaction().isActive()){
			manager.getTransaction().commit();
			manager.clear();		// ---- esvazia o cache de objetos ----
		}
	}
	public static void rollback(){
		if(manager.getTransaction().isActive())
			manager.getTransaction().rollback();
	}

	public void lock(T obj) {
		//usado somente no controle de concorrencia persimista
		manager.lock(obj,LockModeType.PESSIMISTIC_WRITE); 
	}

}

