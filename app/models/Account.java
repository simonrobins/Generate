package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

@Entity
public class Account
{
	@Id
	@Column(name = "account_id")
	public int id;

	public String name;

	public String download;

	@OneToMany(mappedBy = "account")
	public List<AccountContact> contacts;

	public static Account find(final int companyId)
	{
		return Ebean.find(Account.class).where().eq("id", companyId).eq("download", "Y").findUnique();
	}

	public static List<Account> findAll()
	{
		String sql = "SELECT a.account_id, a.name FROM account a " + "join account_contact ac on a.account_id = ac.account_id " + "where a.download = 'Y' and ac.username is not null and ac.password is not null " + "group by a.account_id, a.name " + "order by a.name";

		RawSql rawSql = RawSqlBuilder
			.parse(sql)
			.columnMapping("a.account_id", "id")
			.columnMapping("a.name", "name")
			.create();
		Query<Account> query = Ebean.find(Account.class);
		query.setRawSql(rawSql);
		
		return query.findList();
	}
}
