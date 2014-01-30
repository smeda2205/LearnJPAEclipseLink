package com.learn.jpa.simple.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class RelationshipExample {
	private static final String PERSISTENCE_UNIT_NAME = "people";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		// read the existing entries
		Query q = em.createQuery("select m from Person m");
		// Persons should be empty

		// do we have entries?
		boolean createNewEntries = (q.getResultList().size() == 0);

		// No, so lets create new entries
		if (createNewEntries) {
			Family family = new Family();
			family.setDescription("Family for the Knopfs");
			em.persist(family);
			for (int i = 0; i < 40; i++) {
				Person person = new Person();
				person.setFirstName("Jim_" + i);
				person.setLastName("Knopf_" + i);
				em.persist(person);
				// now persists the family person relationship
				family.getMembers().add(person);
				em.persist(person);
				em.persist(family);
			}
		}

		// Commit the transaction, which will cause the entity to
		// be stored in the database
		em.getTransaction().commit();

		// read the existing entries and write to console
		Query q1 = em.createQuery("select m from Person m");

		List<Todo> personsList = q1.getResultList();
		System.out.println("Size: " + personsList.size());

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();

	}
}
