package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;

@Entity
@Table(name = "account_contact")
public class AccountContact implements Comparable<AccountContact>
{
	@Id
	@Column(name = "account_contact_id")
	public int id;

	@ManyToOne
	@JoinColumn(name = "account_id")
	public Account account;

	public String username;

	public String password;

	public static List<AccountContact> findUsersForCompany(final int companyId)
	{
		return Ebean.find(AccountContact.class).where().isNotNull("username").isNotNull("password").eq("account.id", companyId).findList();
	}

	@Override
	public int compareTo(AccountContact o)
	{
		return username.compareTo(o.username);
	}
}
