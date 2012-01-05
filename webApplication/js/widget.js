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
    
    Person = Backbone.Model.extend({ });
    
    Persons = Backbone.Collection.extend({
    	initialize: function(){
    		this.add(new Person({name:'Andreas Knuth'}));
    		this.add(new Person({name:'Alexander Krumeich'}));
    		this.add(new Person({name:'Stefan Grager'}));
    		this.add(new Person({name:'Dirk Schaube'}));
    	}
    });
    
    Month = Backbone.Model.extend({  
    	initialize: function(){
        	this.name = new XDate(2012, this.get('index')).toString('MMMM', 'de');
    		this.daysOfMonth = XDate.getDaysInMonth (2012, this.get('index') );
        	this.startDay = new XDate(2012,this.get('index'),1).getDay();
        	this.days = new Days({},{actualMonth: this});
        }
    });
    
    Months = Backbone.Collection.extend({
        model:Month,
    	initialize: function(){
        	this.reset();
    		for ( var i=0; i<12 ; i++){
        		this.add(new Month({index:i}));
        	}
        }
    });
    
    Scheduler = Backbone.Model.extend({
    	initialize: function(){
    		this.months = new Months();
    		this.persons = new Persons();
    	}
    });
    
    Day = Backbone.Model.extend({
    	url: "/rest/fp"
    });
    
    Days = Backbone.Collection.extend({
    	model : Day,
    	url: '/rest/fp/days/',
    	initialize: function(models, options){
        	//this.reset();
        	//var dayList = new Days();
    		this.url = this.url+options.actualMonth.name;
    		this.fetch({success: function(){
    			view.render();
    		}});
        	this.actualMonth=options.actualMonth;
        	this.holidays = new Holidays();
        	for ( var i=0; i< this.actualMonth.daysOfMonth ; i++){
            	var t = (this.actualMonth.startDay+i)%7;
            	var hs = this.holidays.get(this.actualMonth.get('index'));
            	//this.holidays.dts.indexOf(new XDate(2012,this.actualMonth.get('index'),i+1))>-1
            	var at = (t==0||t==6||hs.indexOf(i)>-1)?1:0;
            	var d = new Day({index:i, absenseType: at, month:this.actualMonth.name});
            	this.add(d);
        	}
        }
    });
    
    DayView = Backbone.View.extend({ 
    	
    	initialize : function(canvas,day,yindex) {
    		this.canvas = canvas;
    		// create an empty reference to a Raphael circle element
            this.element = this.canvas.circle();
            // overwrite the default this.el by pointing to the DOM object in Raphael
            this.el = this.element.node;
            // now that we have overwritten the default this.el DOM object, 
            // we need to rebind the events
            this.delegateEvents(this.events);
            
    		this.absenseTypes = new AbsenseTypes();
    		this.day = day;
    		this.yindex = yindex;
    		this.selectedColorIndex = -1;
    	},
    	events: { 
                "click": "click" 
        }, 
        click: function(){ 
        	this.selectedColorIndex=selectedAbsenseType;   
        	this.day.save();
        	this.render(); 
        }, 
        render: function(){ 
        	var abstype = this.day.get('absenseType');
        	if (abstype!=1 || this.selectedColorIndex==-1){
            	var color = this.absenseTypes.colors[this.selectedColorIndex==-1?abstype:this.selectedColorIndex];
            	var index = this.day.get('index');
                this.element.attr({cx:index*30+70, cy:this.yindex*40+40, r:8, fill: color});
        	}
            return this.element; 
        } 

    }); 
    
    DaysView = Backbone.View.extend({
    	initialize : function(canvas,scheduler, yindex) {
    		this.canvas = canvas;
            this.yindex = yindex;
            this.scheduler = scheduler;
            this.days = scheduler.get('actualMonth').days; 
            this.elements = [];
    	},
    	render: function(){ 
            this.days = this.scheduler.get('actualMonth').days; 
    		for ( var i=0; i<this.elements.length ; i++){
    			this.elements[i].remove();
    		}
    		for ( var i=0; i<this.days.length ; i++){
            	var view = new DayView(this.canvas,this.days.at(i),this.yindex);
            	this.elements[i] = view.render();
        	}
    	}
    });
    
    AbsenseTypesSelectionView = Backbone.View.extend({
    	initialize : function(canvas) {
    		this.canvas=canvas;
    		this.absenses = new AbsenseTypes();
    		this.abt = [];
    		this.outercircle = [];
    		for ( var i=0; i<this.absenses.type.length ; i++){
    			this.abt[i] = this.canvas.circle();
    			this.outercircle[i] = this.canvas.circle();
    			this.abt[i].node.id='abt_'+i;
    		}
            this.el = document.getElementById("canvas");
            this.delegateEvents(this.events);
    	},
    	events: { 
            "click #abt_0,#abt_1,#abt_2,#abt_3,#abt_4,#abt_5,#abt_6,#abt_7" : "click"
    	}, 
    	click: function(e){ 
    		var clickedEl = $(e.currentTarget);
    		var id = clickedEl.attr("id");
    		selectedAbsenseType = id.substr(4);
    		this.render();
    		
    	}, 
    	render: function(){
    		for ( var i=0; i<this.absenses.type.length ; i++){
    			(this.abt[i]).attr({cx:i*80+20, cy:460, r:8, fill: this.absenses.colors[i]})
   				this.outercircle[i].attr({cx:this.abt[i].attr('cx'),cy:this.abt[i].attr('cy'),r:10, stroke:'#b4cbec',"stroke-width":3,hue: .45});
    			if (i!=selectedAbsenseType){
    				this.outercircle[i].hide();
    			} else {
    				this.outercircle[i].show();
    			}
    			this.canvas.text(i*80+20, 480, this.absenses.type[i]).attr({"font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
    		}
    	}
    	
    });
    
    MonthPaginationView = Backbone.View.extend({
    	initialize : function(canvas,scheduler) {
            this.canvas = canvas;
            this.scheduler = scheduler;
            this.x=800;
            this.y=455;
            
            this.bg = this.canvas.rect();
            this.month = this.canvas.text();
            this.rightc = this.canvas.circle();
            this.leftc = this.canvas.circle();
            this.right = this.canvas.path();
            this.left = this.canvas.path();
            
            this.rightc.node.id = 'rightc';
            this.leftc.node.id = 'leftc';
            this.right.node.id = 'right';
            this.left.node.id = 'left';
            
            this.el = document.getElementById("canvas");
            this.delegateEvents(this.events);
            this.actualMonth = this.scheduler.get('actualMonth');
    	},
    	events: { 
            "click #rightc,#right" : "clickR",
            "click #leftc,#left" : "clickL"
    	}, 
    	clickR: function(){ 
    		this.actualMonth = this.scheduler.get('actualMonth');
    		var am=(this.actualMonth.get('index')==11)?new Month({index:0}):new Month({index:this.actualMonth.get('index')+1});
    		this.scheduler.set({actualMonth:am});
            //this.month.attr({text: this.months.values[this.actualMonth]});
            this.month.attr({text: am.name});
    	}, 
    	clickL: function(){ 
    		this.actualMonth = this.scheduler.get('actualMonth');
    		var am=(this.actualMonth.get('index')==0)?new Month({index:11}):new Month({index:this.actualMonth.get('index')-1});
    		this.scheduler.set({actualMonth:am});
            //this.actualMonth=this.actualMonth==0?11:this.actualMonth-1;
            //this.month.attr({text: this.months.values[this.actualMonth]});
            this.month.attr({text: am.name});
    	}, 
    	render: function(){
            this.bg.attr({x:this.x,y:this.y,width:134,height:26,r:13,fill: "#666", stroke: "none"});

            this.left.attr({path:"M"+(this.x+17)+","+(this.y+8)+"l-10,5 10,5z",fill: "#000"});
            this.right.attr({path:"M"+(this.x+117)+","+(this.y+8)+"l10,5 -10,5z",fill: "#000"});
            
            //this.month.attr({x:this.x+67, y:this.y+13, text: this.months.values[this.actualMonth], fill: "#fff", stroke: "none", "font": '100 18px "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif'});
            this.month.attr({x:this.x+67, y:this.y+13, text: this.actualMonth.name, fill: "#fff", stroke: "none", "font": '100 18px "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif'});

            this.leftc.attr({cx:this.x+13,cy:this.y+13,r:10, fill: "#fff", stroke: "none"});
            this.rightc.attr({cx: this.x+121, cy:this.y+13, r:10, fill: "#fff", stroke: "none"});
            
    		return this;
    	}
    	
    });
    
    ScheduleView = Backbone.View.extend({
    	initialize : function() {
            this.canvas = new Raphael(document.getElementById("canvas"));
            this.scheduler = new Scheduler({actualMonth:new Month({index:new XDate().getMonth()})});
            
            this.persons = this.scheduler.persons;
    		//this.months = new Months(new XDate().getMonth());
            //this.actualMonth = new Month(new XDate().getMonth()); 
        	this.weekdays = ['So','Mo','Di','Mi','Do','Fr','Sa'];
    		this.monthPaginationView = new MonthPaginationView(this.canvas,this.scheduler);
    		this.absenseTypesSelectionView = new AbsenseTypesSelectionView(this.canvas);
    		this.dayNames = [];
    		this.daysviews = [];
    		//********************************************
    		//now let's render()
    		//********************************************
    		this.render();
    		//********************************************
    		//now let's bind()
    		//********************************************
    		this.render1 = _.bind(this.render, this); 
    		this.scheduler.bind('change:actualMonth',this.render1);
    	},
    	render: function(){ 
    		//this.canvas = new Raphael(document.getElementById("canvas"));
    		
    		this.monthPaginationView.render();
    		this.absenseTypesSelectionView.render();
    		for ( var i=0; i<31; i++){
                var t = (this.scheduler.get('actualMonth').startDay+i)%7;
                if (this.dayNames[i]){
                	this.dayNames[i].remove();
                }
                if (i<this.scheduler.get('actualMonth').daysOfMonth){
                    this.dayNames[i] = this.canvas.text();
                    this.dayNames[i].attr({x:i*30+70,y:15,text:this.weekdays[t]+"\n"+(i+1)+".","font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});//this.canvas.text(i*30+70, 15, this.weekdays[t]+"\n"+(i+1)+".").attr({"font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
                }
            }
        	for ( var i=0; i<this.persons.length ; i++){
                var name = this.persons.at(i).get('name').replace(' ','\n');
        		this.canvas.text(30, i*40+40, name).attr({"font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
        		if (!this.daysviews[i]){
        			this.daysviews[i] = new DaysView(this.canvas,this.scheduler,i);
        		}
        		this.daysviews[i].render();
        	}
    	}  	
    	
    });
    
    var selectedAbsenseType = 0; 
    XDate.locales['de'] = {
       	monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember']
    };
    var scheduleView = new ScheduleView();
});