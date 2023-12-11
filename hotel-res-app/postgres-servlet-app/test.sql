insert into Chambre (nom, typeLit, prixBase) values ('LaCarotte', 'Double', '75');
insert into Chambre (nom, typeLit, prixBase) values ('LaBet', 'Queen', '90');
insert into Chambre (nom, typeLit, prixBase) values ('LeCeleri', 'King', '100');
insert into Chambre (nom, typeLit, prixBase) values ('LaPommme', 'simple', '50');

insert into Commodite (description, surplusPrix) values ('TV', 15);
insert into Commodite (description, surplusPrix) values ('Bar', 10);

insert into ChambreCommodite (idChambre, idCommodite) values ('1', '1');
insert into ChambreCommodite (idChambre, idCommodite) values ('1', '2');
insert into ChambreCommodite (idChambre, idCommodite) values ('2', '2');

insert into Reservation (dateDebut, dateFin, idClient, idChambre) values ('2019-05-25', '2019-07-31', '3', '1');