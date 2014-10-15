package transaction.EmailTransaction;

import helper.serviceHelper.EmailSender;
import helper.transactionHelper.EmailMemberProfileHelper;

import javax.persistence.EntityManager;

import model.Member;
import persistence.DAO;
import transaction.DAOTransaction;

public class ProfileInvititionTransaction extends DAOTransaction {

	@Override
	protected Object process(EntityManager em, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		String fromID = (String) params[0];
		String toID = (String) params[1];
		DAO dao = new DAO(em);
		Member invitor = dao.get(Member.class, fromID);
		Member member = dao.get(Member.class, toID);

		String subject = invitor.getAttribute("name") + "邀请你添加照片到CampuSite";
		String toAddr = member.getAttribute("email");

		StringBuffer sb = new StringBuffer();
		sb.append(params[2]);
		sb.append(new EmailMemberProfileHelper().generateMemberProfile(invitor));

		new EmailSender().send(subject, sb.toString(), toAddr);

		return null;
	}

}
