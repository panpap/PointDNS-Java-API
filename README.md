# PointDNS-Java-API
pointhq.com Java API library.

Requires Bouncy Castle library for Base64 codec

Usage:
========================
At first create PointDNS object with your credentials:


`PointDNS pdns=new PointDNS(username,apiKey);`

Zone Examples:
------------------------
Get all zones:
`pdns.getZones();`

Get a specific zone (by name or zone_id):


`pdns.getZone("example.com");`

Get all records of a zone (by name or zone_id):


`pdns.getZoneRecords("example.com"));`

Records Examples:
------------------------
Get Record: 


`pdns.getRecord("example.com", 141);`

Create Record"


`pdns.createRecord("example.com","1.2.3.4","A",3600)); `

Update Record
pdns.updateRecord("example.com", 141, "1.2.3.5")

Delete record:
pdns.deleteRecord("example.com", 141)
