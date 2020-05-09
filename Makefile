# Makefile to create a zip archive with submission files

# Mandatory files
CRACKERFILES=PasswordCrack.java passwd2-plain.txt

# Add any additional files with java classes here
CRACKEREXTRAS=

AUTHFILES=.kaka .base.xml

cracker.zip: $(CRACKERFILES) $(CRACKEREXTRAS)
ifeq ("$(wildcard $(AUTHFILES))","")
	@echo Authentication files missing. Make sure to run from
	@echo directory where challenge was extracted.
else
	zip $@ $^ $(AUTHFILES)
endif
