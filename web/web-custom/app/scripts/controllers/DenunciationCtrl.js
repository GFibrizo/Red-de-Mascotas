'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('DenunciationCtrl', function($scope,$position,$state,$modal,$log,DenunciationsService) {
      
    console.log("DenunciationCtrl")
      
    $scope.data = {
      denunciations: []
    };
    
    $scope.init = function(){
        DenunciationsService.getDenunciations().then(
            function succes(response){
              $scope.data.denunciations = response;

              angular.forEach($scope.data.denunciations, function(value, key) {
                
                if (value.publication.type == "FOR_ADOPTION") {
                    value.publication.type = "En adopción"
                } else if (value.publication.type == "LOST") {
                    value.publication.type = "Perdido"
                } else {
                    value.publication.type = "Encontrado"
                }

                value.date = value.date.substring(0,10);
                DenunciationsService.getImage(value.publication.images[0])
                  .then(
                    function(success){
                        console.log(success);
                        value.publication.image = success.data;
                  });
              });
            }
        );
    }

    $scope.init();   

    
    $scope.accept = function(size,denunciation){        
      console.log("showMessageAccept")
      console.log(denunciation)
      var action = {
        status: true,
        name: "Aceptar"
      };
      var modalInstance = $modal.open({
        templateUrl: 'myModalContent.html',
        controller: 'ModalInstanceActionCtrl',
        size: size,
        resolve: {
              denunciation: function () {
                return denunciation;
              },
              action: function(){
                return action
              }
            }
        });
        modalInstance.result.then(
            function (selectedItem) {
              $scope.init();
            }
        );
      };
    

    $scope.reject = function(size,denunciation){        
      console.log("Denuncias")
      console.log(denunciation)
      var action = {
        status: true,
        name: "Rechazar"
      };
      var modalInstance = $modal.open({
        templateUrl: 'myModalContent.html',
        controller: 'ModalInstanceActionCtrl',
        size: size,
        resolve: {
          denunciation: function () {
            return denunciation;
          },
          action: function(){
            return action
          }
        }
      });      
      modalInstance.result.then(
            function (selectedItem) {
              $scope.init();
            }
        );
    }

  $scope.open = function (size,denunciation) {
    var action = {
      status: false,
      name: ""
    };
    var modalInstance = $modal.open({
      templateUrl: 'myModalContent.html',
      controller: 'ModalInstanceCtrl',
      size: size,
      resolve: {
        denunciation: function () {
          return denunciation;
        },
        action: function(){
          return action
        }
      }
    });
  }
  	
  });


angular.module('sbAdminApp')
.controller('ModalInstanceCtrl', 
    function ($scope, $http, $modalInstance,$base64, denunciation,action,DenunciationsService) {

  $scope.denunciation = denunciation;
  $scope.action = action;
  console.log($scope.denunciation)
 /* DenunciationsService.getImage(denunciation.publication.images[0])
  .then(
    function(success){
        console.log(success);
        denunciation.publication.image = success.data;
  });      
  */
  $scope.ok = function () {
    $modalInstance.close($scope.denunciation);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

angular.module('sbAdminApp')
.controller('ModalInstanceActionCtrl', 
    function ($scope, $modalInstance, denunciation,action,DenunciationsService) {

  $scope.denunciation = denunciation;
  $scope.action = action;
  console.log($scope.denunciation)
  $scope.ok = function () {
    DenunciationsService.acceptDenunciation($scope.denunciation)
    .then(
      function successCallback(response) {
          console.log(response);
           $modalInstance.close(response);
      }, 
      function errorCallback(response) {
          console.log(response);
           $modalInstance.close(response);
      }
    );
  };

  $scope.cancel = function () {
    DenunciationsService.rejectDenunciation($scope.denunciation)
    .then(
      function successCallback(response) {
          console.log(response);
      }, 
      function errorCallback(response) {
          console.log(response);
      }
    );
  };
});
