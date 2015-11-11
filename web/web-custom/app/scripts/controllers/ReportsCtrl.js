'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('ReportsCtrl', function($scope,$position,$state, ReportsService) {
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
/*
        $scope.radioModel = {
            both: true,
            dogs: false,
            cats: false
        }*/
        $scope.radioModel = 'both';
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };



        $scope.formats = ['dd/MM/yyyy',,'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];

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
            console.log($scope.radioModel);
            var filters = {
                date: {
                    to: $scope.date.to,
                    from: $scope.date.from
                },
                type: ReportsService.getNameByCheck($scope.radioModel)
            }
            console.log(filters);
        }
    
  });
