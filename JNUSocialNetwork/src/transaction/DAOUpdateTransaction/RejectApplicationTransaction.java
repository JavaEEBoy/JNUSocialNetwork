package transaction.DAOUpdateTransaction;

import helper.serviceHelper.sendEmailHelper.EmailSender;
import helper.serviceHelper.sendEmailHelper.EmailType;

import javax.persistence.EntityManager;

import model.Application;
import persistence.DAO;
import transaction.DAOTransaction;

public class RejectApplicationTransaction extends DAOTransaction {

	@Override
	protected Object process(EntityManager em, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		DAO dao = new DAO(em);
		Application application = dao.get(Application.class, params[0]);

		String email = application.getAttribute("email");
		String reason = (String) params[1];

		new EmailSender().send("Sorry! Your application is rejected!", reason,
				email, EmailType.TEXT);

		application.delete();
		application.clearAttributes();
		dao.update(application);
		return null;
	}

}
