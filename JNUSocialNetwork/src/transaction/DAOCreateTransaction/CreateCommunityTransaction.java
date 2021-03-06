package transaction.DAOCreateTransaction;

import helper.serviceHelper.NumberManager;
import helper.serviceHelper.searchHelper.CommunitySearchMap;

import java.util.HashMap;

import javax.persistence.EntityManager;

import model.Community;
import model.Member;
import model.factory.ModelFactory;
import model.modelType.UserType;
import persistence.DAO;
import transaction.DAOTransaction;

public class CreateCommunityTransaction extends DAOTransaction {

	@Override
	protected Object process(EntityManager em, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		DAO dao = new DAO(em);
		Member co = dao.get(Member.class, params[0]);
		if (co.getUserType().equals(UserType.MEMBER))
			return new HashMap<String, Object>();
		Community community = ModelFactory.getInstance().create(
				Community.class, params[1], params[2], params[3]);
		co.createCommunity(community);
		dao.update(co);
		CommunitySearchMap.deserialize();
		CommunitySearchMap.addRecord(community.getAttribute("name"),
				community.getID() + "");
		CommunitySearchMap.serialize();
		NumberManager.incrementCommunityNum();
		return community.toRepresentation();
	}

}
