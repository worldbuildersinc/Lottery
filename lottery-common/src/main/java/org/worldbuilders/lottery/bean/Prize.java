package org.worldbuilders.lottery.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by brendondugan on 4/16/17.
 */
@Data
@Entity
@Table(name = "Prize")
public class Prize {
	@Id
	@Column
	private UUID id;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private Boolean isClaimed = false;
	@Column
	private Boolean allowDuplicates = false;
}
