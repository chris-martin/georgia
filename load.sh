#!/bin/bash
psql -d georgia -U georgia -f orgtypes.sql
psql -d georgia -U georgia -f 2008.sql
psql -d georgia -U georgia -f 2009.sql
psql -d georgia -U georgia -f 2010.sql
psql -d georgia -U georgia -f 2011.sql
psql -d georgia -U georgia -f 2012.sql
