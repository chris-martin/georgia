#!/bin/bash
psql -d georgia -U georgia -f src/main/sql/schema.sql
