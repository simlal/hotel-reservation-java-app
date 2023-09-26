**Client**
|cle|nom|type|
|---|---|---|
|PK|idClient|int|
||prenom|varchar(255)|
||nom|varchar(255)|
||age|int|

<br>

**Chambre**
|cle|nom|type|
|---|---|---|
|PK|idChambre|int|
||nom|varchar(255)|
||typeLit|char(1)|
||prixBase|float8|

<br>

**Reservation**
|cle|nom|type|
|---|---|---|
|PK|idReservation|int|
||dateDebut|date|
||dateFin|date|
|FK1|idClient|int|
|FK2|idChambre|int

<br>

**Commodite**
|cle|nom|type|
|---|---|---|
|PK|idCommodite|int|
||description|varchar(255)|
||surplusPrix|float8|

<br>

**ChambreCommodite**
|cle|nom|type|
|---|---|---|
|PK, FK1|idChambre|int|
|PK, FK2|idCommodite|int|
||nombre|int|