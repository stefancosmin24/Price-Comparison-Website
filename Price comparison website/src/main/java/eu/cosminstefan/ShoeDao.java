package eu.cosminstefan;


//Hibernate imports

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class ShoeDao {
    SessionFactory sessionFactory;

    public Shoe simpleSave(Shoe shoe){
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //Add Shoe to database
        session.saveOrUpdate(shoe);

        //Commit transaction
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Shoe added to database with Style Code: " + shoe.getStyleCode());
        return shoe;
    }


    /** Saves and merges data from shoe, shoe instance and shoe classes */
    public void saveAndMerge(Comparison comparison) throws Exception {                                // shoe=university
        //Get a new Session instance from the session factory and start transaction                   //shoe instance= degree
        Session session = sessionFactory.getCurrentSession();                                         //comparison = student
        session.beginTransaction();

        //First find or create shoe
        String queryStr = "from Shoe where style_code='" + comparison.getShoeInstance().getShoe().getStyleCode() + "'";
        //queryStr += " and style_code=" + comparison.getShoeInstance().getShoe().getStyleCode() + "'";
        List<Shoe> shoeList = session.createQuery(queryStr).getResultList();
        //System.out.println("***********" + comparison.getShoeInstance().getShoe() + "*************");
        //shoe is in database
        if(shoeList.size() == 1) {//Found a single shoe
            //Update shoe location, if we want to
            shoeList.get(0).setModel(comparison.getShoeInstance().getShoe().getModel());
            shoeList.get(0).setBrand(comparison.getShoeInstance().getShoe().getBrand());

            //Set mapped shoe in shoe instance
            comparison.getShoeInstance().setShoe(shoeList.get(0));
        }
        //No shoe with that name in database
        else if (shoeList.size() == 0){
            session.saveOrUpdate(comparison.getShoeInstance().getShoe());
        }
        //Error
        else{
            throw new Exception("Multiple shoes with the same name");
        }

        //Next find or create shoe instance. Need to search by shoe id to handle problem
        //of shoe instances with the same name at different shoe.
        queryStr = "from ShoeInstance where shoe_id ='" + comparison.getShoeInstance().getShoe().getStyleCode() + "'";
       // queryStr += " and size=" + comparison.getShoeInstance().getSize() + "'";
        List<ShoeInstance> shoeInstanceList = session.createQuery(queryStr).getResultList();
        //System.out.println(shoeList.size());
        //shoe instance is in database
        if(shoeInstanceList.size() == 1) {//Found a single shoe instance
            //Update shoe instance description, if we want to
            shoeInstanceList.get(0).setSize(comparison.getShoeInstance().getSize());

            //Set mapped shoe in shoe instance
            comparison.setShoeInstance(shoeInstanceList.get(0));
        }
        //No shoe instance with that name in database
        else if (shoeInstanceList.size() == 0){
            session.saveOrUpdate(comparison.getShoeInstance());
        }
        //Error
        else{
            throw new Exception("Multiple shoe instances with the same name");
        }

        //Finally save or update comparison
        queryStr = "from Comparison where comparison_id='" + comparison.getComparisonId() + "'";
        queryStr += " and url='" + comparison.getUrl() + "'";
        List<Comparison> comparisonList = session.createQuery(queryStr).getResultList();

        //comparison is in database
        if(comparisonList.size() == 1) {//Found a single comparison
            //Update comparison if necessary
            if(comparisonList.get(0).getPrice() <= comparison.getPrice())
            comparisonList.get(0).setPrice(comparison.getPrice());//Update mapped comparison object
            comparisonList.get(0).setUrl(comparison.getUrl());
            //comparisonList.get(0).setName(comparison.getName());
            //System.out.println("Price: *******" + comparisonList.get(0).getPrice());
        }
        //No comparison with that name in database
        else if (comparisonList.size() == 0){
            //Create new comparison
            session.saveOrUpdate(comparison);
        }
        //Error
        else{
            throw new Exception("Multiple comparisons with the same first name and surname");
        }

        //Commit transaction to save changes made to mapped classes
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Comparison ID: " + comparison.getComparisonId() + "; Shoe Instance ID: " + comparison.getShoeInstance().getId() + "; Shoe ID: " + comparison.getShoeInstance().getShoe().getStyleCode());
    }





    public void init(){
        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file.
            //Here we are using a configuration file that specifies Java annotations.
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            //Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                //Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory,
                        but we had trouble building the SessionFactory, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy( registry );
            }

            //Ouput result
            System.out.println("Session factory built.");

        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

}
