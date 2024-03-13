package jpql;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class); //타입 정보를 받을 수 있을 때 TypeQuery 사용
            Query query3 = em.createQuery("select m.username, m.age from Member m"); //타입 정보를 받을 수 없을 때 Query 사용

            List<Member> resultList = query1.getResultList(); //값이 여러 개 반환될 때 / 결과가 없으면 빈 리스트 반환 -> NullPointerException 걱정 no
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            Member result = query1.getSingleResult();//값이 하나만 반환될 때 / 결과가 정확하게 하나가 나와야 함 -> 결과가 없으면 NoResultException, 둘 이상이면 NonUniqueResultException
            System.out.println("result = " + result);


            //파라미터 바인딩
//            query1.setParameter("username", "member1");
//            Member result2 = query1.getSingleResult();
//            System.out.println("result2 = " + result2.getUsername());

            ///보통 체이닝을 해서 사용 - 이름 기준, 위치 기준 - 위치 기준은 사용 X
            Member result3 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("result3 = " + result3.getUsername());


            System.out.println("=======");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
