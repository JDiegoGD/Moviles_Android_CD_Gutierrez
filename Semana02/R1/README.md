### Analiza y Responde

* ¿por qué nombre y precio son val pero cantidad es var? ¿Qué
  pasaría si intentas cambiar el precio después de crear el producto?

**El nombre y el precio se definen con val ya que durante la aplicación no se les reasignara un valor nuevo, por otro lado
la cantidad es definida con var ya que su valor puede cambiar las veces necesarias al ejecutar la aplicación. Si se intenta
cambiar el precio después de crear el producto este mismo el compilador generará un error como este: "Val cannot be reassigned"
(val no puede ser reasignado) y el código no se compila.**