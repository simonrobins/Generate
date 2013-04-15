package models;

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

	public static AccountContact find(final int userId)
	{
		return Ebean.find(AccountContact.class, userId);
	}

	@Override
	public int compareTo(AccountContact o)
	{
		return username.compareTo(o.username);
	}
}
