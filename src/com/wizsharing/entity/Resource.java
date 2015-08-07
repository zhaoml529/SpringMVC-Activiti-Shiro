package com.wizsharing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: Resource
 * @Description:资源表
 * @author: zml
 * @date: 2015-6-6 下午15:59:48
 *
 */

@Entity
@Table(name = "T_RESOURCE")
public class Resource implements Serializable {

	private static final long serialVersionUID = 53889172259830160L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ID_SEQ")	//GenerationType.AUTO 有程序决定生成主键
	@SequenceGenerator(name="ID_SEQ", sequenceName="SEQ_RESOURCE_ID", allocationSize = 1)
	@Column(name = "id", length = 5, nullable = false, unique = true)
	private Integer id; 							//编号
	
	@Column(name = "name", length = 50, nullable = false, unique = true)
	@NotEmpty(message = "{resource.name.not.empty}")
    private String name; 							//资源名称
	
	@Column(name = "type", length = 20, nullable = false)
    private String type;							//资源类型
	
	@Column(name = "url", length = 100)
    private String url; 							//资源路径
	
	@Column(name = "permission", length = 100, nullable = false)
	@NotEmpty(message = "{resource.permission.not.empty}")
    private String permission; 						//权限字符串
	
	@Column(name = "parentId", length = 5, nullable = false)
	@NotNull(message = "{resource.parentId.not.empty}")
    private Integer parentId; 						//父编号
	
	@Column(name = "note", length = 300)
    private String note; 							//说明
	
	@Column(name = "available", length = 2, nullable = false)
    private Integer available;
    
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public boolean isRootNode() {
        return parentId == 0;
    }

}
