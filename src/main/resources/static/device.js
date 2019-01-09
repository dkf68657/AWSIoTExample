/**
 * 
 */
var stompClient = null;

var sessionId = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/aws-iot-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/bulbstatus', function (messagePayload) {
        	var obj = JSON.parse(messagePayload.body);
        	reportDeviceBulb(obj.name, obj.value);
        });
    });
}

function reportDeviceBulb(deviceName, value) {
	if(value == 'true'){
		/* on the device.*/
		$('#'+deviceName).removeClass("off-device");
		$('#'+deviceName+'-text').text('On');
	} else {
		/* off the device.*/
		$('#'+deviceName).addClass("off-device");
		$('#'+deviceName+'-text').text('Off');
		
	}
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function getSessionID(){
	$.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/sessionid",
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
        	sessionId = data.name;        	
        },
        error: function (e) {
           
        }
	});
}

function getDeviceStatus() {
	$.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/device/getLastStatus",
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
        	//"red-bulb": false, "green-bulb": false, "blue-bulb": false 
        	setDefaultDeviceStatus('red',data['red-bulb']);
        	setDefaultDeviceStatus('green',data['green-bulb']);
        	setDefaultDeviceStatus('blue',data['blue-bulb']);
        },
        error: function (e) {
           
        }
	});
}


function setDefaultDeviceStatus(deviceName, value) {
	if(value == 'true'){
		/* on the device.*/
		$('#'+deviceName).removeClass("off-device");
		$('#'+deviceName+'-text').text('On');
		$('#'+deviceName+'-chk').prop('checked', true); 
	} else {
		/* off the device.*/
		$('#'+deviceName).addClass("off-device");
		$('#'+deviceName+'-text').text('Off');
		$('#'+deviceName+'-chk').prop('checked', false); 
	}
}

function sendBulbStatus(bulbType, status){
	var bulbTypeUrl = '/device/'+bulbType+'BulbStatus';
	$.ajax({
        type: "GET",
        contentType: "application/json",
        url: bulbTypeUrl,
        dataType: 'json',
        data : "status="+encodeURIComponent(status),
        timeout: 600000,
        success: function (data) {
        	console.log('test');
        	sessionId = data.name;        	
        	console.log('test');
        },
        error: function (e) {
        }
	});
}



function onOffDevice(obj){
	$(obj).toggleClass("off-device");
	var deviceName = $(obj).attr('id');
	var deviceStatus = $(obj).hasClass("off-device");
	var status = deviceStatus ? 'Off' : 'On'; 
	var status_b = deviceStatus ? 'false' : 'true'; 
	console.log(deviceName + ' is on/off :'+ deviceStatus+ ' ---- '+status)
	sendBulbStatus(deviceName,status_b);
	$('#'+deviceName+'-text').text(status);
}


function updateDesiredStatus(obj){
	var status = $(obj).is(":checked");
	var bulbName = $(obj).attr("name"); /*+"-bulb"*/;
	$.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/device/updateDesiredValueOfBulb",
        dataType: 'json',
        data : "bulb="+encodeURIComponent(bulbName)+"&status="+encodeURIComponent(status),
        timeout: 600000,
        success: function (data) {
        	sessionId = data.name;        	
        },
        error: function (e) {
        }
	});
}

$(function () {
	
	connect();
	
	$( "#red").click(function() { onOffDevice(this); });
    $( "#green").click(function() { onOffDevice(this); });
    $( "#blue").click(function() { onOffDevice(this); });
    
    $( "#red-chk").click(function() { updateDesiredStatus(this); });
    $( "#green-chk").click(function() { updateDesiredStatus(this); });
    $( "#blue-chk").click(function() { updateDesiredStatus(this); });
    
    getDeviceStatus();
});
