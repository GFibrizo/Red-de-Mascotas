'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('ReportsCtrl', function($scope,$position,$state) {
      console.log("ReportsCtrl")
      

        //Data
       $scope.bar = {
            labels: ['Adopcion', 'Adoptado', 'Encontrado', 'Hallado'],
            series: ['Series A'],
            data: [
               [65, 59, 80, 81],//ambos               
            ],
            dataByPets: {
              dogs: [65, 59, 80, 81],
              cats: [65, 59, 80, 81]
            },
            options: { scaleShowVerticalLines: false }          
        };

        $scope.date = {
            from: "",
            to: ""
        }

        $scope.average = {
            adoption: "1 dia",
            found: "1 dia"
        }

        $scope.checkModel = {
            both: true,
            dogs: false,
            cats: false
        }

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[3];

        //Functions
        $scope.init = function() {
            $scope.date.to = new Date();
            $scope.date.from = new Date();
            $scope.date.from.setMonth($scope.date.from.getMonth()-1);
        };
        $scope.init();

        $scope.clear = function () {
            $scope.date.to = null;
            $scope.date.from = null;
        };

        // Disable weekend selection
         $scope.disabledFrom = function(date, mode) {
           return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function() {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        
        $scope.toggleMin();

        $scope.openFrom = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.openedFrom = true;
        };      

        $scope.openTo = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.openedTo = true;
        };

        $scope.search = function () {
            console.log($scope.date.to);
            console.log($scope.date.from);
            console.log($scope.checkModel);
        }
    
  });
