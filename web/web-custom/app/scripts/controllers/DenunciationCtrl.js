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
    
    DenunciationsService.getDenunciations().then(
        function succes(response){
          $scope.data = response.data;
        }
    );

    
    $scope.showMessageAccept = function(result){        
      console.log("showMessageAccept")
      console.log(result)
      alert("Aceptar denuncia")
    }

    $scope.showMessage = function(result){        
      console.log("Denuncias")
      console.log(result)
      alert("Rechazar denuncia")
    }

  $scope.open = function (size,denunciation) {

    var modalInstance = $modal.open({
      templateUrl: 'myModalContent.html',
      controller: 'ModalInstanceCtrl',
      size: size,
      resolve: {
        denunciation: function () {
          return denunciation;
        }
      }
    });

    modalInstance.result.then(function (selectedItem) {
      $scope.selected = selectedItem;
    }, function () {
      $log.info('Modal dismissed at: ' + new Date());
    });
  };
  	
  });
angular.module('sbAdminApp').controller('ModalInstanceCtrl', function ($scope, $modalInstance, denunciation) {

  $scope.denunciation = denunciation;
  console.log($scope.denunciation)
  $scope.ok = function () {
    $modalInstance.close($scope.denunciation);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});