# Fiddler

# Arquitectura

La aplicación consta de una clase principal donde se encuentra el 
main llamada **Fiddler**. Esta clase se encarga de llamar a la clase
**ProxyServer** quien es la responsable de realizar toda la lógica para
ejecutar la solicitud del cliente, obtener la respuesta desde el
servidor y devolver la misma al cliente.

# Como correr la app

Para correr la aplicación es necesario tener instalado Java
Development Kit (JDK) versión 8. El mismo se puede encontrar
en la página oficial: **http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html**

Luego descargarse el archivo .jar
que se encuentra en el proyecto. Finalmente abrir una terminal y dirigirse
a la ubicación de ese archivo y ejecutar el siguiente comando:

**java -jar fiddle.jar**

Así queda ejecutándose el proxy y usted podra observar los bytes
transferidos entre el cliente y el servidor, siempre y cuando el 
browser esté bien configurado.

# Configuración de Chrome

Para configurar el browser, lo que se debe hacer es ir a la 
sección de Configuración -> Avanzada <br>

En esta sección seleccionar Abrir la configuración de proxy. 
Se abrirá una nueva ventana, en la misma seleccionar el protocolo
**Proxy de web (HTTP)** y configurar el servidor con los datos: 
- IP: localhost 
- Puerto: 8080 

De esta manera, queda configurado el proxy en el browser.
