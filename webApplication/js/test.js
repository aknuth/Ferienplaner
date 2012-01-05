$(function() {
	
    AbsenseTypes = Backbone.Model.extend({
    	initialize: function(){
        	this.colors = ['#ffffff','gray','red','green','blue','yellow','#000080','#ff9900'],
        	this.type = ['Arbeit','Sa/So/Feiertag', 'Tarifurlaub','Gleittag','Schulung','Sonderurlaub','Geschäftsreise','Sonstiges']
        }
    });

    Holidays = Backbone.Model.extend({
        initialize: function(){
        	 this.dts = [new XDate(2012,0,1),new XDate(2012,3,6),new XDate(2012,3,9),new XDate(2012,4,17),new XDate(2012,4,28),new XDate(2012,5,7),new XDate(2012,9,3),new XDate(2012,10,1),new XDate(2012,11,24),new XDate(2012,11,25),new XDate(2012,11,26),new XDate(2012,11,31)];
        },
    	get: function(month){
    		var result = [];
    		var k=0;
    		for ( var i=0; i<this.dts.length ; i++){
    			if (this.dts[i].getMonth()==month){
    				result[k]=this.dts[i].getDate()-1;
    				k++;
    			}
    		}
    		return result;
    	}
    });

    Day = Backbone.Model.extend({
    	url:'/rest/fp/day',
    	
    });

    Days = Backbone.Collection.extend({
		model:Day,
    	url:'/rest/fp/days',
	    initialize: function(){
	    	this.holidays = new Holidays();
	    	for ( var k=0; k< 12 ; i++){
	    		var monthname = new XDate(2012, k).toString('MMMM', 'de');
	    		var numberDays = XDate.getDaysInMonth (2012, k );
	    		var startDay = new XDate(2012,k,1).getDay();
	    		var hs = this.holidays.get(k);
	    		for ( var i=0; i< numberDays ; i++){
	    			var t = (startDay+i)%7;
	    			var at = (t==0||t==6||hs.indexOf(i)>-1)?1:0;
		    		var day = new Day({day:i, month: k, absenseType: at});
		    		day.save();
		    		this.add(day);
		    	}
	    	}
	    }
	});
	
//    Month = Backbone.Model.extend({
//    	url:'/rest/fp/month',
//    });
//    
//    Months = Backbone.Collection.extend({
//        model:Month,
//        url:'/rest/fp/months',
//        initialize: function(){
//        	for ( var i=0; i< 12 ; i++){
//        		var month = new Month({name:new XDate(2012, i).toString('MMMM', 'de')});
//        		//month.save();
//        		this.add(month);
//        	}
//        }
//    });

	
/**	Scheduler = Backbone.Model.extend({
		url:'/rest/fp',
		initialize: function(){
    		this.months = new Months();
    	}
    });*/

    XDate.locales['de'] = {
           	monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember']
    };
	var days = new Days();
});