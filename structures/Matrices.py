
"""
	This class allows us to represent a path between two places

	categorie : The type of road
	distance  : The path distance
"""
class Arete():
	def __init__(self,categorie, distance):
		self.categorie = categorie
		self.distance = distance

"""
	This class allows us to represent a place
	
	categorie : The type of place
	nom       : The place name
"""
class Noeud():
	def __init__(self,categorie, nom):
		self.categorie = categorie
		self.nom = nom

	"""
		Method to check the equality of two objects
	"""
	def __eq__(self, other):
		return self.nom == other.nom


"""
	This class allows us to represent an entire graph
	
	matrice : Two-dimensional table linking two nodes
	sommet  : Table containing all the graph's nodes
	chemin  : The path to the file containing the graph data
"""
class Graphe():
	def __init__(self, chemin):
		self.matrice = None
		self.sommet = None
		self.chemin = chemin

	"""
		A method to extract graph data from a file
	"""
	def open(self):

		# First opening to determine the number of nodes
		with open(self.chemin, 'r') as file:
			compteur = 0
			for ligne in file.readlines():
				compteur += 1

		# Creation of the matrix with the right size
		self.matrice = [[None for j in range(compteur)] for i in range(compteur)]
		# Creation of the table of nodes
		self.sommet = [None for j in range(compteur)] 

		# Second opening to determine all available nodes
		with open(self.chemin, 'r') as file:
			for indice,ligne in enumerate(file.readlines()):

				# Extraction of the node's data (1st value before the ":") without carriage return
				ligne = ligne.rstrip('\n').split(':')[0]

				# Add node data to the table
				self.sommet[indice] = Noeud(*(ligne.split('/')))

		# last opening to add neighbors of each nodes
		with open(self.chemin, 'r') as file:
			for indice,ligne in enumerate(file.readlines()):

				#  Loop that runs to recoveries of all neighbors without a return carriage
				for voisin in ligne.rstrip('\n').split(':')[1:]:

					# Extraction and association of the data on neighbors
					categorie,distance,genre,nom = voisin.split('/')

					# Search in the table of nodes where the path goes
					index = self.sommet.index(Noeud(categorie,nom))

					# Adding the path in the matrix
					self.matrice[indice][index] = Arete(categorie,distance)
					
	"""
		A method to display all the information of the graph 
	"""	
	def __str__(self):
		taille = max([len(i.nom) for i in self.sommet])+5
		case = "{:>" + str(taille) + "} | "

		texte = " "
		texte += case.format("")
		for element in self.sommet:
			texte += case.format(element.nom + " [" + element.categorie +"]")
		texte += '\n'
		for indice, element in enumerate(self.matrice):
			texte += " " + case.format(self.sommet[indice].nom + " [" + self.sommet[indice].categorie + "]")
			for val in element:
				if val != None:
					texte += case.format(val.distance + " km [" + val.categorie+ "]")
				else:
					texte += case.format("- ")
			texte += '\n'
		return texte

monGraphe = Graphe("Graphe.csv")
monGraphe.open()
print(monGraphe)