var year = 2013;
var month = 1;
var personenID = 0;
//Liste von Personen, ueber personenListe.length kann die Anzahl ermittelt werden
var personenListe = new Array();
var jahr2013 = new Array(12);
var colors = ['#7fad13','#79bbff','#fe1a00','#da3df6','#3d94f6','#ffce79','#ff8000'];
var abwesenheitstyp = 0;
var urlaubstage = 0;
var flag = true;
/**
 * Funktion die beim Start ausgeführt wird
 **/
$(function() {
    start();
});

/**
 * Diese Funktion erzeugt beim ersten Aufruf alle Personen und die Darstellung des aktuellen Monats
 * mit allen Spalten und Feldern
 */ 
function start() {
    testePersonenHinzufuegen();
    initBelegungsliste();
    erzeugePersonSpalte();
    renderMonth();
    getMonthData();
    
}

//eingabeort für die personen die angelegt werden sollen
//hier eingegebene personen werden als spalten generiert
function testePersonenHinzufuegen(){
    addPerson("Andreas");
    addPerson("Yannick");
    addPerson("Stefan");
}

// fuegt der Liste eine Person hinzu, welche in der Funktion createPerson(..) gebildet wurde
function addPerson(personenName) {
    personenID++;
    personenListe.push(createPerson(personenName,personenID));
}

//erzeugt eine person die sich aus name und id= laufende nummer zusammen setzt
function createPerson(name, id) {
    // Ein array aus Namen/Tagen 
    var person = new Array(id, name);
    return person;
}

//erzeugt eine spalte für jede angegebene person
function erzeugePersonSpalte() {
    //Anhand der ID kann die Person wieder aus der Personenliste ermittelt werden
    for (var i = 0; i < personenListe.length; i++) {
        $('div#listen').append('<ul id="' + personenListe[i][1] + '" class="view"><li class="text"><div>' + personenListe[i][1] + '</div></li></ul>');
        
    }
}

//generiert die datumsanzeige und die tagesfelder für die gegebene anzahl an personen
function renderMonth() {
    var anzahlTageMonat = daysInMonth(year, month);
    for (var i = 0; i < anzahlTageMonat; i++) {
        //belegteTageImMonatP1[i] = 'Arbeit';
        generateDayfields(i + 1);
        appendTag(i);
    }

    $('span[id^="type"]').click(function() {
        abwesenheitstyp = parseInt($(this).attr('id').substring(4));
        $('span[id^="type"]').each(function() {
            $(this).css('border', 'none');
        });
        $(this).css('border', 'solid 2px black');
    });
    $('li[id^="day"]').click(function() {
        $(this).css('background-color', colors[abwesenheitstyp - 1]);
        //alert(((this.id).substring(3) % 31) + ' , ' + month + ' , ' + year + ' , ' + (personenListe[(parseInt((this.id).substring(3) - ((this.id).substring(3) % 31)) / 31)]) + ' , ' + abwesenheit);
        var day = this.id.substring(3) % 31;
        var personID = personenListe[(parseInt((this.id).substring(3) - ((this.id).substring(3) % 31)) / 31)][0];
        var oldAWIndex = parseInt($(this).attr('abwesenheit'));
		var ferientage = parseInt($('#zaehler_'+personID).text());
		
        if (abwesenheitstyp===7 && oldAWIndex!=7){
    		ferientage = ferientage +1;
    		
    	} else if (abwesenheitstyp!=7 && oldAWIndex===7){
    		ferientage = ferientage -1;
    		
    	}
		
		$('#zaehler_'+personID).text(ferientage);
        $(this).attr('abwesenheit',''+abwesenheitstyp);
        $.get("/rest/fp/2013/"+month+"/"+day+"/"+abwesenheitstyp+"/"+personID, function(data) {
        })
    });
    if (flag){
        $('a[id^="month"]').click(function(e) {
        	if ($(this).attr('id').substring(6)==='zurueck'){
        		previousMonth();
        	} else {
        		nextMonth();
        	}
        	getMonthData();
        });
    }
    flag = false;
    $('li#monat').replaceWith('<li id="monat" class="h4">' + monthName(month - 1) + " " + year + '</li>');
    appendUrlaubstagezaehler();
}

function getMonthData(){
	$.get("/rest/fp/"+month, function(data) {
        for (var i=0;i<data.length;i++){
        	if(data[i].month == month){
	        	var year = data[i].year;
	            var id = data[i].day+(31*(data[i].personenID-1));
	            $('li#day'+id).css('background-color',''+colors[(data[i].abwesenheit-1)]);
	            $('li#day'+id).attr('abwesenheit',''+data[i].abwesenheit);
	            if(data[i].abwesenheit == 7){
	            	var ferientage = parseInt($('#zaehler_'+data[i].personenID).text());
	        		ferientage = ferientage +1;
	        		
	            	$('#zaehler_'+data[i].personenID).text(ferientage);
	            }
	        }
    	}
    })
}

function appendUrlaubstagezaehler(){
    for(var i = 1; i<=personenListe.length; i++){
        $('ul#'+ personenListe[i -1][1]).append('<li id="zaehler_'+i+'" class="zaehler" onclick="alert(id)">'+urlaubstage+'</li>');
    }
}

//errechnet anzahl der tage im monat
function daysInMonth(year, month) {
    return new Date(year, month, 0).getDate();
}

//generiert tagesfelder mit vortlaufender id
function generateDayfields(idTagesFeld) {
    var idZaehler = idTagesFeld;
    //VORSICHT: setzt voraus das immer ein Person existiert
    // id einer Person ist auch die id des UL
    var zaehler = 1;
    while (zaehler <= personenListe.length) {
        if (dayOfWeek(year, month, idTagesFeld) == 'Mo') {
            $('ul#' + personenListe[zaehler - 1][1]).append('<li  id=day' + idZaehler + ' class="view2"/>');
        }
        else {
            if (dayOfWeek(year, month, idTagesFeld) == 'So') {
                $('ul#' + personenListe[zaehler - 1][1]).append('<li  id=day' + idZaehler + ' class="view2"/>');
            }
            else {
                $('ul#' + personenListe[zaehler - 1][1]).append('<li  id=day' + idZaehler + ' class="view"/>');
            }
        }
        zaehler = zaehler + 1;
        idZaehler = idZaehler + 31;
    }
}


//generiert datumsanzeige
function appendTag(day) {
    $('ul#tage').append('<li class="text"><div>' + dayOfWeek(year, month, day) + '</div><div>' + (day + 1) + '.</div></li>');
}

//errechnet wochentag für jedes datum
function dayOfWeek(year, month, day) {
    var currentDay = new Date(year, month - 1, day + 1).getDay();
    var days = ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'];
    return days[currentDay];
}

//gibt den monatsnamen aus
function monthName(month){
    var months = ["Januar", "Febuar", "M&auml;rz", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"];
    return months[month];
}

//wechselt zum nächsten monat
function nextMonth() {
    if (month == 12) {
        year = year + 1;
        month = 1;
    }
    else {
        month = month + 1;
    }
    renewMonth();
}

//wechselt zum vorherigen monat
function previousMonth() {
    if (month == 1) {
        year = year - 1;
        month = 12;
    }
    else {
        month = month - 1;
    }
     renewMonth();
}

 //entfernt tagesfelder, rendert tagesfelder für den folgemonat neu
function renewMonth(){
    //1. entfernen der TagesFelder
    removeLis();
    //2. Aufbau der Felder Monatsaktuell
    erzeugePersonSpalte();
    renderMonth();
    $('li#monat').replaceWith('<li id="monat" class="h4">' + monthName(month - 1) + " " + year + '</li>');
    //3. Belegung der Felder mit bereits gewählten Stati aus vorherigen Userinteraktionen
}

//entfernt tagesdatum und tagefelder
function removeLis() {
    removeDaysInView();
    removeLine();    

}

//entfernt datumsanzeige
function removeDaysInView(){
     $('ul#tage li:not(:first-child)').remove();
}

//entfernt die ganze Zeile inkl. Name und Datumsfelder
function removeLine() {
    for (var i = 0; i < personenListe.length; i++) {
        $('ul#'+personenListe[i][1]).remove();
    }
}

/**
 * Erstellt ein Array welches ein Jahr auf Monatsebene repräsentiert. 
 * Die Monate enthalten alle Tagesfelder welcher in der Oberfläche zu Auswahl stehen.
 **/
function initBelegungsliste() {


    //Januar
    jahr2013[0] = new Array(daysInMonth(2013, 1) * personenListe.length);
    //Febuar
    jahr2013[1] = new Array(daysInMonth(2013, 2) * personenListe.length);
    //Maerz
    jahr2013[2] = new Array(daysInMonth(2013, 3) * personenListe.length);
    //April
    jahr2013[3] = new Array(daysInMonth(2013, 4) * personenListe.length);
    //Mai
    jahr2013[4] = new Array(daysInMonth(2013, 5) * personenListe.length);
    //Juni
    jahr2013[5] = new Array(daysInMonth(2013, 6) * personenListe.length);
    //Juli
    jahr2013[6] = new Array(daysInMonth(2013, 7) * personenListe.length);
    //August
    jahr2013[7] = new Array(daysInMonth(2013, 8) * personenListe.length);
    //September
    jahr2013[8] = new Array(daysInMonth(2013, 9) * personenListe.length);
    //Oktober
    jahr2013[9] = new Array(daysInMonth(2013, 10) * personenListe.length);
    //November
    jahr2013[10] = new Array(daysInMonth(2013, 11) * personenListe.length);
    //Dezember
    jahr2013[11] = new Array(daysInMonth(2013, 12) * personenListe.length);


    // belege alle Monate vor
    for (var monatZaehler = 0; monatZaehler < 12; monatZaehler++) {
        for (var zaehler = 0; zaehler < (daysInMonth(2013, monatZaehler + 1) * personenListe.length); zaehler++) {
            jahr2013[monatZaehler][zaehler + 1] = "Arbeit";
        }
    }
    for (var k = 1; k <= 12; k++) {
        var currentDayIndex = new Date(2013, k-1, 1).getDay(); //0=Sonntag 6=Samstag
        for (var j = 0; j < personenListe.length; j++) {
            var indexErsterSamstag = (6 - currentDayIndex);
            for (var i = 1; i <= (daysInMonth(2013, k) - 1); i++) {
                //erster Tag war Sonntag
                if (indexErsterSamstag == 6 && i - 1 == 0) {
                    jahr2013[k-1][1] = "Feiertag";
                }
                else if (i % indexErsterSamstag == 0) {
                    jahr2013[k-1][i + 1 + (daysInMonth(2013, k) * j)] = "Feiertag";
                    jahr2013[k-1][i + 2 + (daysInMonth(2013, k) * j)] = "Feiertag";
                    indexErsterSamstag = indexErsterSamstag + 7;
                }
            }
        }
    }

}