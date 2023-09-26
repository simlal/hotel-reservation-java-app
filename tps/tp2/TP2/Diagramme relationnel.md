**Client**
|cle|nom|type|constraint|
|---|---|---|---|
|PK|idClient|int||
||prenom|varchar(255)|not null|
||nom|varchar(255)|not null|
||age|int|age > 0|

<br>

**Chambre**
|cle|nom|type|constraint|
|---|---|---|---|
|PK|idChambre|int|
||nom|varchar(255)|not null|
||typeLit|char(1)|not null|
||prixBase|float8|not null, prixBase > 0|

<br>

**Reservation**
|cle|nom|type|constraint|
|---|---|---|---|
|PK|idReservation|int|
||dateDebut|date|
||dateFin|date|dateFin > dateDebut
|FK1|idClient|int|
|FK2|idChambre|int

<br>

**Commodite**
|cle|nom|type|constraint|
|---|---|---|---|
|PK|idCommodite|int|
||description|varchar(255)|
||surplusPrix|float8|not null, surplusPrix > 0|

<br>

**ChambreCommodite**
|cle|nom|type|
|---|---|---|
|PK, FK1|idChambre|int|
|PK, FK2|idCommodite|int|
||nombre|int|