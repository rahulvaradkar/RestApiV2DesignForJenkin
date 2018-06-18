(function( module, test ) {

	module('Module 1');

	test('test 1', function(assert) {
		
		assert.equal(1, 1, 'Assert 1 = 1');
		
	});

	test('test 2', function(assert) {
	
		assert.equal(1, 1, 'Assert 1 = 1');
		assert.equal(1, 1, 'Assert 1 = 1');
	});
	

})( QUnit.module, QUnit.test );
