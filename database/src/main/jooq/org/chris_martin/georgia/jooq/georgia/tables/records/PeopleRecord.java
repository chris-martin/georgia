/**
 * This class is generated by jOOQ
 */
package org.chris_martin.georgia.jooq.georgia.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class PeopleRecord extends org.jooq.impl.TableRecordImpl<org.chris_martin.georgia.jooq.georgia.tables.records.PeopleRecord> implements org.jooq.Record4<java.lang.String, java.lang.String, java.math.BigDecimal, java.math.BigDecimal> {

	private static final long serialVersionUID = -372395487;

	/**
	 * Setter for <code>georgia.people.person_name</code>. 
	 */
	public void setPersonName(java.lang.String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>georgia.people.person_name</code>. 
	 */
	public java.lang.String getPersonName() {
		return (java.lang.String) getValue(0);
	}

	/**
	 * Setter for <code>georgia.people.job_title</code>. 
	 */
	public void setJobTitle(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>georgia.people.job_title</code>. 
	 */
	public java.lang.String getJobTitle() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>georgia.people.salary</code>. 
	 */
	public void setSalary(java.math.BigDecimal value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>georgia.people.salary</code>. 
	 */
	public java.math.BigDecimal getSalary() {
		return (java.math.BigDecimal) getValue(2);
	}

	/**
	 * Setter for <code>georgia.people.travel</code>. 
	 */
	public void setTravel(java.math.BigDecimal value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>georgia.people.travel</code>. 
	 */
	public java.math.BigDecimal getTravel() {
		return (java.math.BigDecimal) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.math.BigDecimal, java.math.BigDecimal> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.math.BigDecimal, java.math.BigDecimal> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return org.chris_martin.georgia.jooq.georgia.tables.People.PEOPLE.PERSON_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.chris_martin.georgia.jooq.georgia.tables.People.PEOPLE.JOB_TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field3() {
		return org.chris_martin.georgia.jooq.georgia.tables.People.PEOPLE.SALARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field4() {
		return org.chris_martin.georgia.jooq.georgia.tables.People.PEOPLE.TRAVEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value1() {
		return getPersonName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getJobTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value3() {
		return getSalary();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value4() {
		return getTravel();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PeopleRecord
	 */
	public PeopleRecord() {
		super(org.chris_martin.georgia.jooq.georgia.tables.People.PEOPLE);
	}
}
