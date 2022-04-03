
"""
	This class allows us to represent a path between two places

	categorie : The type of road
	distance  : The path distance
	ref       : The pointer to the arrival place
	suivant   : The pointer to another possible path
"""
class Arete:
	def __init__(self, categorie, distance, ref, suivant):
		self.categorie = categorie
		self.distance = distance
		self.ref = ref
		self.suivant = suivant


"""
	This class allows us to represent a place
	
	categorie : The type of place
	nom       : The place name
	voisin    : A list of paths from this place
"""
class Noeud:
	def __init__(self, categorie, nom):
		self.categorie = categorie
		self.nom = nom
		self.voisin = None

	"""
		Method to insert a new neighbor at this location
	"""
	def insertVoisin(self, categorie,distance,ref):
		self.voisin = Arete(categorie,distance,ref,self.voisin)


"""
	This class allows us to represent an entire graph

	noeuds  : A dictionary linking the name of a place with its object
	chemin  : The path to the file containing the graph data
"""
class Graphe:
	def __init__(self,chemin):
		self.noeuds = {}
		self.chemin = chemin

	"""
		A method to extract graph data from a file
	"""
	def open(self):

		# First opening to determine all available nodes
		with open(self.chemin, 'r') as file:
			for ligne in file.readlines():
				# Extraction of the node's data (1st value before the ":") without carriage return
				noeud = ligne.rstrip('\n').split(':')[0]

				# Associating the data to variables
				categorie,nom = noeud.split('/')

				# Adds to the dictionary a node associated with its name
				self.noeuds[nom] = Noeud(categorie,nom)

		# Second opening to determine the neighbors of each place
		with open(self.chemin, 'r') as file:
			for ligne in file.readlines():
				# Separation of the line into two parts (node | neighbors) without carriage return
				nom, voisins = ligne.rstrip('\n').split(':',1)

				# Extraction of the node's name
				nom = nom.split('/')[1]

				# Loop that runs through all the neighbors
				for voisin in voisins.split(':'):
					# Extraction and association of the data on neighbors
					link, kilometrage, categorie, destination = voisin.split('/')

					# Insertions of its data in the right node
					self.noeuds[nom].insertVoisin(link,kilometrage,self.noeuds[destination]) 

	"""
		A method to display all the information of the graph 
	"""	
	def __str__(self):
		taille = max([len(i) for i in self.noeuds]) + 2
		case = "{:" + str(taille) + "}"
		texte = ""
		if len(self.noeuds)>0:
			for noeud in self.noeuds.values():
				texte += f"[{noeud.categorie}] {case.format(noeud.nom)} => "
				voisin = noeud.voisin
				while(voisin != None):
					texte += f" {voisin.categorie} | {voisin.distance:>3} km | {case.format(voisin.ref.nom)}"
					voisin = voisin.suivant
					if voisin != None:
						texte += ' -> '
				texte += '\n'
		else:
			texte = "Aucune valeur"
			
		return texte
		

monGraphe = Graphe("Graphe.csv")
monGraphe.open()
print(monGraphe)