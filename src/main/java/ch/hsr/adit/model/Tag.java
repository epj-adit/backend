package ch.hsr.adit.model;
// Generated 23.03.2017 08:47:58 by Hibernate Tools 5.2.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Tag generated by hbm2java
 */
@Entity
@Table(name = "tag", schema = "public")
public class Tag implements DbEntity {

	private int id;
	private String name;
	private Set advertisements = new HashSet(0);

	public Tag() {
	}

	public Tag(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Tag(int id, String name, Set advertisements) {
		this.id = id;
		this.name = name;
		this.advertisements = advertisements;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "advertisement_tag", schema = "public", joinColumns = {
			@JoinColumn(name = "tag_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "advertisement_id", nullable = false, updatable = false) })
	public Set getAdvertisements() {
		return this.advertisements;
	}

	public void setAdvertisements(Set advertisements) {
		this.advertisements = advertisements;
	}

}
