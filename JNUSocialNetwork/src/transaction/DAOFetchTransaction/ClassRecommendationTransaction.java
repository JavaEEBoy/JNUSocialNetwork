package transaction.DAOFetchTransaction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import transaction.DAOTransaction;

public class ClassRecommendationTransaction extends DAOTransaction {
	static final int STARTINDEX = 0;
	static final int TOINDEX = 10;
	DAOTransaction transaction;

	@SuppressWarnings("unchecked")
	@Override
	protected Object process(EntityManager em, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> recommendations = new LinkedList<Map<String, Object>>();

		transaction = new RandomlyFetchMemberTransaction();
		List<Map<String, Object>> members = (List<Map<String, Object>>) transaction
				.execute();
		transaction = new FetchMemberTransaction();
		Map<String, Object> member = (Map<String, Object>) transaction
				.execute(params);

		for (Map<String, Object> m : members) {
			Map<String, String> mattributes = (Map<String, String>) m
					.get("attributes");
			Map<String, String> memberAttributes = (Map<String, String>) member
					.get("attributes");
			if (mattributes != null
					&& memberAttributes != null
					&& mattributes.get("major") != ""
					&& mattributes.get("major") != null
					&& mattributes.get("major").equals(
							memberAttributes.get("major"))
					&& mattributes.get("session") != ""
					&& mattributes.get("session") != null
					&& mattributes.get("session").equals(
							memberAttributes.get("session")))
				recommendations.add(m);
		}

		return recommendations.size() > TOINDEX ? recommendations.subList(
				STARTINDEX, TOINDEX) : recommendations;
	}

}