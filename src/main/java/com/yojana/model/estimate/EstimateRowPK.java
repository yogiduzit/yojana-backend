package com.yojana.model.estimate;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

@Embeddable
public class EstimateRowPK implements Serializable {

    private static final long serialVersionUID = -8709713671106036600L;
    
    @Column(name="EstimateId", nullable = false, insertable = false, updatable = false)
    @Type(type="uuid-char")
    private UUID estimateId;
    
    @Column(name = "RowIndex")
    private int index;
    
    

	public EstimateRowPK() {
		super();
	}

	public EstimateRowPK(UUID estimateId, int index) {
		super();
		this.estimateId = estimateId;
		this.index = index;
	}

	public UUID getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(UUID estimateId) {
		this.estimateId = estimateId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estimateId == null) ? 0 : estimateId.hashCode());
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstimateRowPK other = (EstimateRowPK) obj;
		if (estimateId == null) {
			if (other.estimateId != null)
				return false;
		} else if (!estimateId.equals(other.estimateId))
			return false;
		if (index != other.index)
			return false;
		return true;
	}    
}
