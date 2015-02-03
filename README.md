# PointDNS-Java-API
[pointhq] (http://www.pointhq.com) Java API library.


This module provides easy access to point zone & record management.

Requires:
========================
Bouncy Castle library (for Base64 codec)


Authentication:
========================
To access your Point account, you'll need to define your username & apikey. The username is your email address and the apikey is the API token from the My Account tab.


Usage:
========================
At first create PointDNS object with your credentials:

    PointDNS pdns=new PointDNS(username,apiKey);

Zone Examples:
------------------------
Get all zones:

    pdns.getZones();

Get a specific zone (by name or zone_id):

    pdns.getZone("example.com");

Get all records of a zone (by name or zone_id):

    pdns.getZoneRecords("example.com"));

Records Examples:
------------------------
Get Record: 

    pdns.getRecord("example.com", 141);

Create Record"

    pdns.createRecord("example.com","1.2.3.4","A",3600));

Update Record

    pdns.updateRecord("example.com", 141, "1.2.3.5");

Delete record:

    pdns.deleteRecord("example.com", 141);
