#!/usr/bin/env bash
set -eu

eval "FLYWAY_URL=\"$1\""
eval "FLYWAY_USER=\"$2\""
eval "FLYWAY_PASSWORD=\"$3\""
FLYWAY_SCHEMAS=$4
FLYWAY_TABLE=$5
FLYWAY_LOCATIONS=$6

# https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/7.15.0/flyway-commandline-7.15.0.tar.gz
/usr/bin/flyway -community -url="${FLYWAY_URL}" -user="${FLYWAY_USER}" -password="${FLYWAY_PASSWORD}" -placeholderReplacement=false -cleanDisabled=true -baselineOnMigrate=true -baselineVersion=1 -encoding=UTF-8 -outOfOrder=false -validateOnMigrate=true -schemas="${FLYWAY_SCHEMAS}" -table="${FLYWAY_TABLE}" -locations="${FLYWAY_LOCATIONS}" migrate
