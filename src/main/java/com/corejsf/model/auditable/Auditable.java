package com.corejsf.model.auditable;

public interface Auditable {
	public Audit getAudit();
	public void setAudit(Audit audit);
}
