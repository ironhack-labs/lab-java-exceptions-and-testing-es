
![logo_ironhack_blue 7](https://user-images.githubusercontent.com/23629340/40541063-a07a0a8a-601a-11e8-91b5-2f13e4e6b441.png)

# LAB | Java Excepciones y Pruebas (Testing)

## Introducción

Acabamos de aprender cómo crear excepciones personalizadas, cómo manejarlas en controladores y cómo usar MockMVC para probar los controladores, así que practiquemos un poco más.

<br>

## Requisitos

1. Haz un fork de este repositorio.
2. Clona este repositorio.
3. Añade a tu instructor y a los calificadores de la clase como colaboradores de tu repositorio. Si no estás seguro de quiénes son los calificadores de tu clase, pregunta a tu instructor o consulta la presentación del primer día.
4. En el repositorio, crea un proyecto de Java y añade el código para las siguientes tareas.

## Entrega

Una vez que termines la tarea, envía un enlace URL a tu repositorio o tu solicitud de extracción en el campo de abajo.

<br>

## Configuración

Combina los dos laboratorios `4.02` y `4.04` y luego copia el código a tu nuevo repositorio.

<br>

## Especificaciones

1. Prueba todas las rutas disponibles utilizando `MockMVC`.

<br>

## Consejos

Para probar las rutas, puedes seguir estos pasos:

1. Comienza importando las dependencias necesarias para la integración de tests, como `@SpringBootTest` de Spring Boot, `MockMvc` y `WebApplicationContext`.
2. En tu clase de test, usa la anotación `@SpringBootTest` para configurar el entorno de test y crea un objeto de `WebApplicationContext` inyectándolo.
3. A continuación, crea un objeto de `MockMvc` utilizando `MockMvcBuilders.webAppContextSetup(webApplicationContext)`.
4. Usa el objeto `MockMvc` para realizar solicitudes GET, POST y PUT a las diferentes rutas en tu aplicación.
5. Para los casos positivos, usa el método `.andExpect(status().isOk())` para asegurarte de que se devuelva el código de estado adecuado (200 OK) en la respuesta. También puedes usar `.andExpect(content().json("json esperado"))` para verificar si el json devuelto coincide con lo que esperas.
6. Para los casos negativos, usa el método `.andExpect(status().is(400))` para verificar que el código de estado devuelto sea 400 Bad Request. También puedes usar `.andExpect(status().reason(containsString("Bad Request")))` para verificar si el cuerpo de la respuesta contiene el mensaje de error adecuado.
7. Para manejar las variables de ruta y los parámetros de consulta, puedes usar el método `.param("nombreParam", "valorParam")` para agregarlos a la solicitud antes de realizarla.
8. Repite los pasos 4-7 para todas las rutas GET, POST y PUT en tu aplicación, incluyendo las rutas de los Laboratorios 4.02 y 4.04.
9. Ejecuta tus tests y verifica que todas ellas pasen.

Es importante tener en cuenta que los tests deben estar aisladas y no deben tener efectos secundarios en otros tests, por lo que asegúrate de limpiar cualquier dato creado durante el test.

<br>

## FAQs (Preguntas frecuentes)

<br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">Estoy atascado y no sé cómo resolver el problema o por dónde empezar. ¿Qué debo hacer?</summary>

  <br> <!-- ✅ -->

  Si estás atascado en tu código y no sabes cómo resolver el problema o por dónde empezar, debes dar un paso atrás y tratar de formular una pregunta clara y directa sobre el problema específico que enfrentas. El proceso que seguirás al tratar de definir esta pregunta te ayudará a limitar el problema y a encontrar soluciones potenciales.

  Por ejemplo, ¿estás enfrentando un problema porque no entiendes el concepto o estás recibiendo un mensaje de error que no sabes cómo arreglar? Por lo general, es útil intentar formular el problema de la manera más clara posible, incluyendo cualquier mensaje de error que estés recibiendo. Esto puede ayudarte a comunicar el problema a otras personas y, potencialmente, a obtener ayuda de tus compañeros o recursos en línea.

  Una vez que tengas una comprensión clara del problema, deberías poder comenzar a trabajar hacia la solución.

</details>

  <br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">¿Cómo creo un proyecto de Spring boot?</summary>

  <br> <!-- ✅ -->

  Spring boot es un framework para crear aplicaciones autónomas y de calidad de producción que son fáciles de lanzar y ejecutar. La mejor manera de crear un proyecto de Spring boot es usar el sitio web Spring Initializer. El sitio web proporciona una manera conveniente de generar una estructura básica de proyecto con todas las dependencias y configuraciones necesarias.

  - Paso 1: Ve a [start.spring.io](https://start.spring.io/)
  - Paso 2: Elige el tipo de proyecto que desea crear, como Maven o Gradle.
  - Paso 3: Selecciona la versión de Spring Boot que deseas utilizar.
  - Paso 4: Elige las dependencias que necesitas para tu proyecto. Algunas dependencias comunes incluyen web, jpa y data-jpa.
  - Paso 5: Haz clic en el botón "Generar" para descargar los archivos del proyecto.

  Como alternativa, puedes usar un Entorno de Desarrollo Integrado (IDE) como Eclipse o IntelliJ IDEA. Estos IDEs tienen complementos para crear proyectos de Spring boot, lo que facilita la configuración del entorno y el inicio de la codificación.

</details>

  <br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">¿Qué son "MockMvc" y "WebApplicationContext" y cómo podemos utilizarlos en Java?</summary>

  <br> <!-- ✅ -->

  `MockMvc` y `WebApplicationContext` son dos componentes importantes utilizados en el testing de aplicaciones Spring MVC.

  `MockMvc` se utiliza para probar el comportamiento de una aplicación Spring MVC, incluyendo el manejo de solicitudes y respuestas HTTP. `WebApplicationContext` se utiliza para configurar el contexto de la aplicación para fines de testing.

  Para utilizar `MockMvc` y `WebApplicationContext`, se necesita crear una clase de test con el siguiente código:

  ```java
  @SpringBootTest
  public class YourControllerTest {

      @Autowired
      private MockMvc mockMvc;

      @Autowired
      private WebApplicationContext webApplicationContext;

      @Before
      public void setup() {
          mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
      }

      @Test
      public void testExample() throws Exception {
          mockMvc.perform(get("/your-endpoint"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("Hello World!"));
      }
  }
  ```

  En este código, la anotación  `@SpringBootTest` se utiliza para configurar la clase de test para probar una aplicación Spring MVC. Los componentes `MockMvc` y `WebApplicationContext` se inyectan automáticamente y se utiliza un método `setup` para configurar la instancia de `MockMvc` usando el `WebApplicationContext`. Finalmente, se crea un método de test para probar el comportamiento del punto final utilizando el método `mockMvc.perform`.

</details>

  <br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">¿Qué es "ObjectMapper" en Java y cómo se puede usar?</summary>

  <br> <!-- ✅ -->

  `ObjectMapper` es una clase de la biblioteca Jackson que se utiliza para leer y escribir datos JSON en Java. Permite convertir objetos Java a JSON y viceversa. Con `ObjectMapper`, puedes convertir fácilmente objetos a y desde el formato JSON, lo que lo hace una herramienta útil para trabajar con servicios web y APIs RESTful.

  Aquí hay un fragmento de código para demostrar cómo usar `ObjectMapper` para convertir un objeto Java en una cadena JSON:

  ```java
  // Import ObjectMapper class
  import com.fasterxml.jackson.databind.ObjectMapper;

  // Create an instance of ObjectMapper
  ObjectMapper mapper = new ObjectMapper();

  // Convert a Java object to JSON
  User user = new User();
  user.setName("John Doe");
  user.setAge(30);
  String json = mapper.writeValueAsString(user);
  System.out.println(json);
  ```

  Y aquí hay un fragmento de código para demostrar cómo usar `ObjectMapper` para convertir una cadena JSON en un objeto Java:

  ```java
  // Import ObjectMapper class
  import com.fasterxml.jackson.databind.ObjectMapper;

  // Create an instance of ObjectMapper
  ObjectMapper mapper = new ObjectMapper();

  // Convert a JSON string to a Java object
  String json = "{\"name\":\"John Doe\",\"age\":30}";
  User user = mapper.readValue(json, User.class);
  System.out.println(user.getName());
  System.out.println(user.getAge());
  ```

  Como puedes ver, usar `ObjectMapper` es simple y directo. Puedes usarlo para realizar una variedad de tareas relacionadas con la lectura y escritura de datos JSON en Java, lo que lo convierte en una herramienta esencial para cualquier desarrollador de Java que necesite trabajar con servicios web RESTful y APIs.  

</details>
  
  <br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">¿Qué es "SimpleDateFormat" y cómo se puede usar en Java?</summary>

  <br> <!-- ✅ -->

  `SimpleDateFormat` es una clase concreta en Java que permite formatear y analizar fechas. Es parte del paquete `java.text package`.

  `SimpleDateFormat` se puede usar en Java para formatear una fecha en una representación de cadena. Por ejemplo:

  ```java
  Date date = new Date();
  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
  String formattedDate = sdf.format(date);
  System.out.println("Formatted date: " + formattedDate);
  ```

  `SimpleDateFormat` se puede usar para analizar una cadena en una fecha. Por ejemplo:

  ```java
  String dateString = "15-01-2021";
  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
  Date parsedDate = sdf.parse(dateString);
  System.out.println("Parsed date: " + parsedDate);
  ```

  El formato de la cadena de fecha en `SimpleDateFormat` se especifica como un parámetro de cadena cuando se crea el objeto `SimpleDateFormat`. La cadena de formato usa símbolos para representar diferentes partes de una fecha, como día, mes, año, etc. Por ejemplo, la cadena de formato "dd-MM-yyyy" representa una fecha en el formato "día-mes-año".

</details>
  
  <br>

<details>
  <summary style="font-size: 16px; cursor: pointer; outline: none; font-weight: bold;">No puedo enviar cambios a mi repositorio. ¿Qué debo hacer?</summary>

  <br> <!-- ✅ -->

  Si no puedes enviar cambios a tu repositorio, aquí hay algunos pasos que puedes seguir:

  1. Verifica tu conexión a internet: Asegúrate de que tu conexión a internet sea estable y funcione.
  2. Verifica la URL de tu repositorio: Asegúrate de estar usando la URL correcta de tu repositorio para enviar tus cambios.
  3. Revisa tus credenciales de Git: Asegúrate de que tus credenciales de Git estén actualizadas y correctas. Puedes revisar tus credenciales usando el siguiente comando:

    ```bash
    git config --list
    ```

  4. Actualiza tu repositorio local: Antes de enviar cambios, asegúrate de que tu repositorio local esté actualizado con el repositorio remoto. Puedes actualizar tu repositorio local usando el siguiente comando:

    ```bash
    git fetch origin
    ```

  5. Revisa posibles conflictos: Si hay conflictos entre tu repositorio local y el repositorio remoto, resuélvelos antes de enviar cambios.
  6. Envía cambios: Una vez que hayas resuelto los conflictos y actualizado tu repositorio local, puedes intentar enviar cambios nuevamente usando el siguiente comando:

    ```bash
    git push origin <branch_name>
    ```

</details>