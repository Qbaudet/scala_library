package utils

import models.{Book_Entity, User}
import services.Catalog
import upickle.default.*
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

/**
 * Objet `CatalogIO` qui gère l'entrée et la sortie des données dans le système.
 * Cet objet permet de sauvegarder et de charger les livres et les utilisateurs depuis
 * des fichiers JSON, ainsi que de remplacer les données du catalogue en mémoire.
 */
object CatalogIO:
  // Stockage des données
  private val dataDir = "src/data" 

  /**
   * Sauvegarde les livres du catalogue dans un fichier JSON.
   *
   * Cette méthode prend une liste de livres et les sauvegarde dans un fichier JSON situé dans le répertoire `src/test/resources/`.
   * Si le fichier n'existe pas, il sera créé. Le répertoire contenant le fichier sera également créé si nécessaire.
   *
   * @param catalog Le catalogue contenant la liste des livres à sauvegarder.
   * @param fileName Le nom du fichier dans lequel les livres doivent être sauvegardés (par défaut, "test_books.json").
   * @throws Exception Si le fichier ne peut pas être créé.
   */
  def saveBooks(catalog: Catalog, filePath:String): Unit = {
    val path = Paths.get(filePath)
    Files.createDirectories(path.getParent)
    val json = upickle.default.write(catalog.books.toList)
    Files.write(path, json.getBytes(StandardCharsets.UTF_8))

    // On vérifie que le fichier a bien été créé
    if (!Files.exists(path)) {
      throw new Exception(s"Failed to create file at: $path")
    }
  }

  /**
   * Sauvegarde les utilisateurs du catalogue dans un fichier JSON.
   *
   * Cette méthode prend une liste d'utilisateurs et les sauvegarde dans un fichier JSON situé dans le répertoire `src/test/resources/`.
   * Si le fichier n'existe pas, il sera créé. Le répertoire contenant le fichier sera également créé si nécessaire.
   *
   * @param catalog Le catalogue contenant la liste des utilisateurs à sauvegarder.
   * @param fileName Le nom du fichier dans lequel les utilisateurs doivent être sauvegardés (par défaut, "test_users.json").
   * @throws Exception Si le fichier ne peut pas être créé.
   */
  def saveUsers(catalog: Catalog, filePath:String): Unit = {
    val path = Paths.get(filePath)
    Files.createDirectories(path.getParent)
    val json = upickle.default.write(catalog.users.toList)
    Files.write(path, json.getBytes(StandardCharsets.UTF_8))

    // On vérifie que le fichier a bien été créé
    if (!Files.exists(path)) {
      throw new Exception(s"Failed to create file at: $path")
    }
  }

  /**
   * Charge les livres à partir d'un fichier JSON.
   *
   * Cette méthode tente de charger une liste de livres depuis un fichier JSON situé dans le répertoire `src/data/`.
   * Si le fichier n'existe pas, une liste vide est renvoyée.
   *
   * @param fileName Le nom du fichier à charger (par défaut, "books.json").
   * @return Une liste de livres chargée depuis le fichier.
   */
  def loadBooks(filePath:String): List[Book_Entity] =
    val path = Paths.get(filePath)
    if Files.exists(path) then
      val content = Files.readString(path)
      read[List[Book_Entity]](content)
    else List.empty

  /**
   * Charge les utilisateurs à partir d'un fichier JSON.
   *
   * Cette méthode tente de charger une liste d'utilisateurs depuis un fichier JSON situé dans le répertoire `src/data/`.
   * Si le fichier n'existe pas, une liste vide est renvoyée.
   *
   * @param fileName Le nom du fichier à charger (par défaut, "users.json").
   * @return Une liste d'utilisateurs chargée depuis le fichier.
   */
  def loadUsers(filePath:String): List[User] =
    val path = Paths.get(filePath)
    if Files.exists(path) then
      val content = Files.readString(path)
      upickle.default.read[List[User]](content)
    else List.empty

  /**
   * Remplace les données du catalogue avec de nouveaux livres et utilisateurs.
   *
   * Cette méthode remplace les livres et les utilisateurs actuels dans le catalogue par les nouveaux livres et utilisateurs fournis.
   * Elle met à jour les données du catalogue en filtrant les livres existants et en les ajoutant aux nouveaux livres,
   * puis en remplaçant les utilisateurs par ceux fournis.
   *
   * @param catalog Le catalogue à mettre à jour.
   * @param books La liste des livres à ajouter au catalogue.
   * @param users La liste des utilisateurs à ajouter au catalogue.
   */
  def replaceCatalogData(
    catalog: Catalog,
    books: List[Book_Entity],
    users: List[User]
    ): Unit =
    // Remplacer les livres
    val updatedBooks = catalog.books.filterNot(book => books.exists(_.ISBN == book.ISBN))
    catalog.books = updatedBooks ++ books

    // Remplacer les utilisateurs
    catalog.users = users
