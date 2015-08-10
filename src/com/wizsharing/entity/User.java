package com.wizsharing.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName: User
 * @Description:用户实体类
 * @author: zml
 * @date: 2015-8-6 上午11:11:48
 *
 */

@Entity
@Table(name = "T_USER")
public class User implements Serializable{

	private static final long serialVersionUID = -6662232329895785824L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	//GenerationType.AUTO 有程序决定生成主键
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_USER_ID", allocationSize = 1)
	@Column(name = "USER_ID", length = 5, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "USER_NAME", length = 50, nullable = false, unique = true)
	private String name;
	
	@Column(name = "USER_PWD", length = 150, nullable = false)
	@NotEmpty(message = "{user.password.not.empty}")
	//@Pattern(regexp = "[A-Za-z0-9]{5,20}", message = "{user.password.illegal}") 
	private String passwd;
	
	@Column(name = "USER_SALT", length = 100)
	private String salt; // 加密密码的盐
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "REG_DATE")
	private Date registerDate;
	
	@Column(name = "LOCKED", length = 2, nullable = false)
    private Integer locked;
	
	//多对一，@JoinColumn与@column类似，指定映射的数据库字段
	@ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
	@JoinColumn(name="GROUP_ID",updatable=false)
    private Group group;
    
	public User(){
		
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public Date getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}
	
    public String getCredentialsSalt() {
        return name + salt;
    }
}
