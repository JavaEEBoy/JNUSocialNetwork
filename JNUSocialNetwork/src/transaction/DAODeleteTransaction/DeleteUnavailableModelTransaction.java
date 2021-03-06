package transaction.DAODeleteTransaction;

import java.util.List;

import javax.persistence.EntityManager;

import persistence.DAO;
import transaction.DAOTransaction;

public class DeleteUnavailableModelTransaction extends DAOTransaction {
	static final int bucketSize = 10;

	@SuppressWarnings({ "unchecked" })
	@Override
	protected Object process(EntityManager em, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		DAO dao = new DAO(em);
		List<Object> unavailableIDs = dao.combinedRead((String) params[0]);
		System.out.println("unavailableIDs:" + unavailableIDs);

		for (int i = 1, effectRowNums = 0; i <= unavailableIDs.size(); i += bucketSize) {
			try {
				effectRowNums = dao.delete((String) params[1], i
						- effectRowNums, i + bucketSize);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getCause());
				continue;
			}
		}

		return null;
	}

}
