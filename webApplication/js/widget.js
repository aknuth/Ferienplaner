$(function() {

    
    /**DayView = Backbone.View.extend({ 
    	
    	initialize : function(day,yindex,xindex) {

            
    		this.absenseTypes = new AbsenseTypes();
    		this.day = day;
    		this.yindex = yindex;
    		this.xindex = xindex;
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
            	var cc=canvas.circle().attr({cx:this.xindex*30+70, cy:this.yindex*40+40, r:8, fill: color});
            	cc.node.id=cc+this.yindex+this.xindex;
        	}
            return this.element; 
        } 

    });*/ 
    
    DaysView = Backbone.View.extend({
    	initialize : function(days) {
    		this.el = document.getElementById("canvas");
            this.delegateEvents(this.events);
    		this.absenseTypes = new AbsenseTypes();
            this.days=days;
            this.persons=days.persons;
            this.selectedColorIndex = -1;
    	},
    	events: { 
    		"click circle[id^='cc_']" : "click"
    	}, 
    	click: function(e){ 
    		var clickedEl = $(e.currentTarget);
    		var id = clickedEl.attr("id");
    		var x = parseInt(id.substring(3,4));
    		var y = parseInt(id.substring(4,5));
    		var monthLength = this.days.length/persons.length;
    		var index = y*monthLength+x	;
    		var day = this.days.at(index);
    		//var abstype = day.get('absenseType');
    		var color = this.absenseTypes.colors[selectedAbsenseType];
    		var cc = canvas.circle().attr({cx:x*30+70, cy:y*40+40, r:8, fill: color});
    		cc.node.id='cc_'+x+y;
    		this.selectedColorIndex=selectedAbsenseType;   
    		day.set({absenseType: selectedAbsenseType});
    		day.save();
    	}, 
    	render: function(){ 
    		var monthLength = this.days.length/persons.length;
			for ( var k=0; k<persons.length ; k++){
				for ( var i=0; i<monthLength; i++){
        			var day = this.days.at(i);
        			var name = persons.at(k).name.replace(' ','\n');
        			canvas.text(30, k*40+40, name).attr({"font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
        			//var view = new DayView(this.days.at(i+k*monthLength),k, i);
                	//view.render();
        			var day = this.days.at(i+k*monthLength);
                	var abstype = day.get('absenseType');
                	if (abstype!=1 || this.selectedColorIndex==-1){
                    	var color = this.absenseTypes.colors[this.selectedColorIndex==-1?abstype:this.selectedColorIndex];
                    	var cc=canvas.circle().attr({cx:i*30+70, cy:k*40+40, r:8, fill: color});
                    	cc.node.id='cc_'+i+k;
                	}

				}
        	}
    	}
    });
    
    AbsenseTypesSelectionView = Backbone.View.extend({
    	initialize : function() {
    		this.absenses = new AbsenseTypes();
            this.el = document.getElementById("canvas");
            this.delegateEvents(this.events);
    	},
    	events: { 
            //"click #abt_0,#abt_1,#abt_2,#abt_3,#abt_4,#abt_5,#abt_6,#abt_7" : "click"
    		"click circle[id^='abt_']" : "click"
    	}, 
    	click: function(e){ 
    		var clickedEl = $(e.currentTarget);
    		var id = clickedEl.attr("id");
    		selectedAbsenseType = id.substr(4);
    		this.render();
    		
    	}, 
    	render: function(){
    		for ( var i=0; i<this.absenses.type.length ; i++){
    			var ic = canvas.circle().attr({cx:i*80+20, cy:460, r:8, fill: this.absenses.colors[i]});
    			ic.node.id='abt_'+i;
   				var oc = canvas.circle().attr({cx:ic.attr('cx'),cy:ic.attr('cy'),r:10, stroke:'#b4cbec',"stroke-width":3,hue: .45});
    			if (i!=selectedAbsenseType){
    				oc.remove();
    			} else {
    				oc.show();
    			}
    			canvas.text(i*80+20, 480, this.absenses.type[i]).attr({"font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
    		}
    	}
    	
    });
    
    MonthPaginationView = Backbone.View.extend({
    	initialize : function() {
            this.x=800;
            this.y=455;
            
            this.bg = canvas.rect();
            this.month = canvas.text();
            this.rightc = canvas.circle();
            this.leftc = canvas.circle();
            this.right = canvas.path();
            this.left = canvas.path();
            
            
            this.el = document.getElementById("canvas");
            this.delegateEvents(this.events);
            //this.actualMonth = this.scheduler.get('actualMonth');
    	},
    	events: { 
            "click #rightc,#right" : "clickR",
            "click #leftc,#left" : "clickL"
    	}, 
    	clickR: function(){ 
    		var actualMonth = scheduler.get('actualMonth');
    		var am=(actualMonth==11)?0:actualMonth+1;
    		scheduler.set({actualMonth:am});
    	}, 
    	clickL: function(){ 
    		var actualMonth = scheduler.get('actualMonth');
    		var am=(actualMonth==0)?11:actualMonth-1;
    		scheduler.set({actualMonth:am});
    	}, 
    	render: function(){
    		canvas.rect().attr({x:this.x,y:this.y,width:134,height:26,r:13,fill: "#666", stroke: "none"});

            //this.month.attr({x:this.x+67, y:this.y+13, text: this.months.values[this.actualMonth], fill: "#fff", stroke: "none", "font": '100 18px "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif'});
            canvas.text().attr({x:this.x+67, y:this.y+13, text: new XDate(2012, scheduler.get('actualMonth')).toString('MMMM', 'de'), fill: "#fff", stroke: "none", "font": '100 18px "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif'});

            var left = canvas.circle().attr({cx:this.x+13,cy:this.y+13,r:10, fill: "#fff", stroke: "none"});
            var right = canvas.circle().attr({cx: this.x+121, cy:this.y+13, r:10, fill: "#fff", stroke: "none"});
            var leftc = canvas.path().attr({path:"M"+(this.x+17)+","+(this.y+8)+"l-10,5 10,5z",fill: "#000"});
            var rightc = canvas.path().attr({path:"M"+(this.x+117)+","+(this.y+8)+"l10,5 -10,5z",fill: "#000"});

            rightc.node.id = 'rightc';
            leftc.node.id = 'leftc';
            right.node.id = 'right';
            left.node.id = 'left';
            
    		return this;
    	}
    	
    });
    
    ScheduleView = Backbone.View.extend({
    	initialize : function(persons,scheduler) {
    		this.days=new Backbone.Collection;
    		this.days.model=Day;
    		this.scheduler=scheduler;
    		this.persons = persons;
        	this.weekdays = ['So','Mo','Di','Mi','Do','Fr','Sa'];
    		//********************************************
    		//now let's render()
    		//********************************************
    		this.fetch();
    		//********************************************
    		//now let's bind()
    		//********************************************
    		this.render1 = _.bind(this.fetch, this); 
    		this.scheduler.bind('change:actualMonth',this.render1);
    	},
    	fetch: function(){
    		this.days.url = '/rest/fp/days/'+this.scheduler.get('actualMonth');
    		this.days.fetch({success: function(){
    			canvas.clear();
        		monthPaginationView.render();
        		absenseTypesSelectionView.render();
    			scheduleView.render();
    		}, error: function(e) {
    			console.log(e+'error');
    		}});
    	},
    	render: function(){ 
    		var daysOfMonth = XDate.getDaysInMonth (2012, this.scheduler.get('actualMonth'));
    		var startDay = new XDate(2012,this.scheduler.get('actualMonth'),1).getDay();
    		for ( var i=0; i<31; i++){
                var t = (startDay+i)%7;
                if (i<daysOfMonth){
                    canvas.text().attr({x:i*30+70,y:15,text:this.weekdays[t]+"\n"+(i+1)+".","font": '10px Fontin-Sans, Arial', stroke: "none", fill: "#fff"});
                }
            }
        	
			var ds = new Days(this.days, [{persons: persons},{am:this.scheduler.get('actualMonth')}]);
			var dv = new DaysView(ds);
			dv.render();
    	}  	
    	
    });
    
    XDate.locales['de'] = {
           	monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember']
    };

    var selectedAbsenseType = 0; 
    var scheduler = new Scheduler({actualMonth:new XDate().getMonth()}); 
    var scheduleView = null;
    
    var persons = new Persons();
	
    canvas = new Raphael(document.getElementById("canvas"));     
    monthPaginationView = new MonthPaginationView(canvas);
	absenseTypesSelectionView = new AbsenseTypesSelectionView(canvas);
   

    persons.fetch({success: function(){
    	console.log('persons loaded ...');				
    	scheduleView = new ScheduleView(persons,scheduler);
    },error:function (xhr, ajaxOptions, thrownError){
        console.log(xhr.status);
        console.log(thrownError);
    }});
    console.log('nothing');
});