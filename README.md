#http://georgia.chris-martin.org/

This project is an effort to make the data from
[open.georgia.gov](http://www.open.georgia.gov/)
more accessible.

## Progress

The _Salaries and Travel Reimbursements_ section is fully scraped,
and all of the data from 2008 to 2012 has been obtained.

I have not yet looked into what else can be scraped, but it looks like
something similar can be done with _Other Expenditure Information_.

I'm currently in the process of building a web-based API and human interface.

## Repository contents

### `master`

`scrape/` -
A web scraping library which pulls data from open.georgia.gov and stores it in JSON format.

### `json`

The `json` branch contains the most raw dump of the data from the scraping process.

### `sql`

The `sql` branch contains SQL scripts (in PostgreSQL dialect) suitable for filling a
database with schema defined by `database/src/main/sql/schema.sql` in the `master` branch.
