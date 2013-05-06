This project is an effort to make the data from
[open.georgia.gov](http://www.open.georgia.gov/)
more accessible.

## Progress

The _Salaries and Travel Reimbursements_ section is fully scraped,
and all of the data from 2008 to 2012 has been obtained.

I have not yet looked into what else can be scraped, but it looks like
something similar can be done with _Other Expenditure Information_.

The next step will be to build a web interface.
I don't have any specific plans yet.

## Repository contents

### `master` branch

`scrape/` -
A web scraping library which pulls data from open.georgia.gov and stores it in JSON format.

### `data` branch

The `data` branch contains all of the data that has been retrieved.

