var app = angular.module("markr", ['leaflet-directive']);
app.factory('MyService', function($rootScope) {
    var mService = {};
    mService.connection = {};
    mService.connect = function() {
        // use this when deploying your application
        var wsUri = "ws://"+window.location.hostname+"/ws" ;
        // use this when testing local
        // var wsUri = "ws://localhost:9000/ws";
        var ws = new WebSocket ( wsUri ) ;

        ws.onopen = function ( ) {
            console.log("connection established ...");
            mService.connection = ws;
        }
        ws.onmessage = function ( event ) {
            $rootScope.$apply(function() {
                console.log("Received data: " + event.data);
                mService.messages.push ( JSON.parse(event.data) ) ;
            });
        }
    };
    mService.postMsg = function(data) {
        mService.connection.send(data);
    };
    mService.messages = [];
    return mService;
});

app.controller("mainCtrl", function($rootScope, $scope, $http, MyService) {
    MyService.connect();
    $scope.markers = MyService.messages.map(function(tweet) {
        return {
            lng: tweet.lng,
            lat: tweet.lat,
            message: tweet.message,
            focus: true
        }
    });

    $scope.marker = {};

    $rootScope.$watchCollection(
        function() {
            return MyService.messages;
        },
        function(newValue) {
            $scope.markers = newValue.map(function(moveMessage) {
                return {
                    lng: moveMessage.longitude,
                    lat: moveMessage.latitude,
                    message: moveMessage.msg,
                    focus: false
                }
            });
        }
    );

    $scope.sendMessage = function() {
        var data = {
            longitude: $scope.marker.lng,
            latitude: $scope.marker.lat,
            msg: $scope.marker.username
        };

        function isValid(coordinate) {
            return coordinate!=null && coordinate<=175 && coordinate >=-175;
        }

        if (!data.msg.trim.isEmpty && isValid(data.latitude) && isValid(data.longitude)) {
            MyService.postMsg(JSON.stringify(data));
        }
    };
});