import java.util.*;

public class Univers {
	private ArrayList<LevelMove> univ;
	private int taille;
	private int [] worldAcces;

	public Univers() {
		this.univ = new ArrayList<LevelMove>();
		this.taille = 0;
	}

	public Univers(ArrayList<LevelMove> u, int taille) {
		this.univ = new ArrayList<LevelMove>(u);
		this.taille = taille;
	}

	public boolean checkOutside(CoordSet o, Direction d, LevelMove l){
		LevelMove lOut = univ.get(l.getOutsideWorld());
		if(!lOut.checkForMove(CoordSet.AddVec(lOut.getPosWorld(l.getWorldNum()), CoordSet.DirToVec(d)))){
			return false;
		}
		moveOutside(o,d, l);
		return true;

	}

	public void moveOutside(CoordSet o, Direction d, LevelMove l){
		LevelMove lOut = univ.get(l.getOutsideWorld());
		CoordSet nextspot = CoordSet.AddVec(o, CoordSet.DirToVec(d));
		int[][] tpmat = l.getLevelData();
		int[][] tpmatOut = lOut.getLevelData();
		CoordSet ctmp = CoordSet.AddVec(lOut.getPosWorld(l.getWorldNum()), CoordSet.DirToVec(d));
		int tmp = tpmat[o.getX()][o.getY()];
		tpmat[o.getX()][o.getY()] = Cells.VIDE;
		tpmatOut[ctmp.getX()][ctmp.getY()] = tmp; 
	}

	public boolean checkInside(CoordSet o, Direction d, LevelMove l, int monde){
		LevelMove lIn = univ.get(l.getLevelData(o.getX(), o.getY()));
		if(!lIn.checkForMove(lIn.EnterPos(d),d)){
			return false;
		}
		moveOutside(o,d, l);
		return true;

	}

	public void moveInside(CoordSet o, Direction d, LevelMove l){
		LevelMove lIn = univ.get(l.getLevelData(o.getX(), o.getY()));;
		CoordSet nextspot = CoordSet.AddVec(o, CoordSet.DirToVec(d));
		int[][] tpmat = l.getLevelData();
		int[][] tpmatIn = lIn.getLevelData();
		CoordSet ctmp = CoordSet.AddVec(o, CoordSet.DirToVec(CoordSet.revDirection(d)));
		int tmp = tpmat[o.getX()][o.getY()];
		tpmat[o.getX()][o.getY()] = Cells.VIDE;
		tpmatIn[ctmp.getX()][ctmp.getY()] = tmp; 
	}




	public boolean checkMovePossible(CoordSet o, Direction d, LevelMove l) {
		int [][] tmpMat = l.getLevelData();
		LevelMove tmpL;
		CoordSet tmpCoord, tmpCoord2 = new CoordSet(o.getX(), o.getY());


		if(l.checkForMove(o,d)){
			return true;
		}
		CoordSet nextspot = CoordSet.AddVec(o, CoordSet.DirToVec(d));
		if(tmpMat[nextspot.getX()][nextspot.getY()] >= 0){
			if(!checkMovePossible(nextspot, null, l)){

			}
		}

		if (d == Direction.HAUT && o.getX() == 0) {
			tmpL = univ.get(l.getOutsideWorld());
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			
			tmpCoord.addToX(-1);
			
			if (!checkMovePossible(tmpCoord, d, tmpL))
				return false;
			else
				return checkMovePossible(tmpCoord, d, tmpL);
		}

		if (d == Direction.HAUT && tmpMat[o.getX() - 1][o.getY()] != Cells.VIDE) {
			while (tmpMat[o.getX() - 1][o.getY()] != Cells.VIDE) {
				o.addToX(-1);
				if (tmpMat[o.getX()][o.getY()] == Cells.MUR) {
					o.addToX(1);
					break;
				}
				if (o.getX() == 0) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, l.getWorldNum()))
						tmpCoord = tmpL.getPosWorld(l.getWorldNum());
					else
						tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
					
					tmpCoord.addToX(-1);

					if (!checkMovePossible(tmpCoord, d, tmpL)) {
						break;
					}
					else
						return checkMovePossible(tmpCoord, d, tmpL);
				}
				if (tmpMat[o.getX() - 1][o.getY()] == Cells.VIDE) {
					return true;
				}
			}

			//while (tmpMat[o.getX()][o.getY()] != Cells.VIDE && tmpMat[o.getX()][o.getY()] != Cells.JOUEUR) {
			while (!o.equals(tmpCoord2)) {	
				if (l.isAWorld(o.getX(), o.getY())) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					if (checkMovePossible(new CoordSet(tmpL.getSizeMat() - 1, tmpL.getSizeMat() / 2), d, tmpL)) {
						worldAcces[tmpMat[o.getX()][o.getY()]]++;
						return checkMovePossible(new CoordSet(tmpL.getSizeMat() - 1, tmpL.getSizeMat() / 2), d, tmpL);
					}
					else {
						o.addToX(1);
					}
				}
				else {
					o.addToX(1);
				}

				if (o.getX() == l.getSizeMat())
					break;
		
			}
			return false;
		}

		if (d == Direction.BAS && o.getX() == l.getSizeMat() - 1) {
			tmpL = univ.get(l.getOutsideWorld());
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else {
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			}
			tmpCoord.addToX(1);
			if (!checkMovePossible(tmpCoord, d, tmpL))
				return false;
			else
				return checkMovePossible(tmpCoord, d, tmpL);
		}

		if (d == Direction.BAS && tmpMat[o.getX() + 1][o.getY()] != Cells.VIDE) {
			while (tmpMat[o.getX() + 1][o.getY()] != Cells.VIDE) {
				o.addToX(1);
				if (tmpMat[o.getX()][o.getY()] == Cells.MUR) {
					o.addToX(-1);
					break;
				}
				if (o.getX() == l.getSizeMat() - 1) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, l.getWorldNum()))
						tmpCoord = tmpL.getPosWorld(l.getWorldNum());
					else
						tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
					tmpCoord.addToX(1);
					if (!checkMovePossible(tmpCoord, d, tmpL)) {
						break;
					}
					else
						return checkMovePossible(tmpCoord, d, tmpL);
				}
				if (tmpMat[o.getX() + 1][o.getY()] == Cells.VIDE) {
					return true;
				}
			}

			//while (tmpMat[o.getX()][o.getY()] != Cells.VIDE && tmpMat[o.getX()][o.getY()] != Cells.JOUEUR) {
			while (!o.equals(tmpCoord2)) {
				if (l.isAWorld(o.getX(), o.getY())) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					if (checkMovePossible(new CoordSet(0, tmpL.getSizeMat() / 2), d, tmpL)) {
						worldAcces[tmpMat[o.getX()][o.getY()]]++;
						return checkMovePossible(new CoordSet(0, tmpL.getSizeMat() / 2), d, tmpL);
					}
					else {
						o.addToX(-1);
					}
				}
				else {
					o.addToX(-1);
				}

				if (o.getX() == -1)
					break;
			}
			return false;
		}

		
		if (d == Direction.GAUCHE && o.getY() == 0) {
			tmpL = univ.get(l.getOutsideWorld());
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			
			tmpCoord.addToY(-1);
			if (!checkMovePossible(tmpCoord, d, tmpL))
				return false;
			else
				return checkMovePossible(tmpCoord, d, tmpL);
		}
		if (d == Direction.GAUCHE && tmpMat[o.getX()][o.getY() - 1] != Cells.VIDE) {
			while (tmpMat[o.getX()][o.getY() - 1] != Cells.VIDE) {
				o.addToY(-1);
				if (tmpMat[o.getX()][o.getY()] == Cells.MUR) {
					o.addToY(1);
					break;
				}
				if (o.getY() == 0) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, l.getWorldNum()))
						tmpCoord = tmpL.getPosWorld(l.getWorldNum());
					else
						tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
					tmpCoord.addToY(-1);
					if (!checkMovePossible(tmpCoord, d, tmpL)) {
						break;
					}
					else
						return checkMovePossible(tmpCoord, d, tmpL);
				}

				if (tmpMat[o.getX()][o.getY() - 1] == Cells.VIDE) {
					return true;
				}

			}

			//while (tmpMat[o.getX()][o.getY()] != Cells.VIDE && tmpMat[o.getX()][o.getY()] != Cells.JOUEUR) {
			while (!o.equals(tmpCoord2)) {
				if (l.isAWorld(o.getX(), o.getY())) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					if (checkMovePossible(new CoordSet(tmpL.getSizeMat() / 2, tmpL.getSizeMat() - 1), d, tmpL)) {
						worldAcces[tmpMat[o.getX()][o.getY()]]++;
						return checkMovePossible(new CoordSet(tmpL.getSizeMat() / 2, tmpL.getSizeMat() - 1), d, tmpL);
					}
					else {
						o.addToY(1);
					}
				}
				else {
					o.addToY(1);
				}

				if (o.getY() == l.getSizeMat()) 
					break;
			
			}
			return false;
		}

		if (d == Direction.DROITE && o.getY() == l.getSizeMat() - 1) {
			tmpL = univ.get(l.getOutsideWorld());
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			tmpCoord.addToY(1);
			if (!checkMovePossible(tmpCoord, d, tmpL))
				return false;
			else
				return checkMovePossible(tmpCoord, d, tmpL);
		}

		if (d == Direction.DROITE && tmpMat[o.getX()][o.getY() + 1] != Cells.VIDE) {
			while (tmpMat[o.getX()][o.getY() + 1] != Cells.VIDE) {
				o.addToY(1);
				if (tmpMat[o.getX()][o.getY()] == Cells.MUR) {
					o.addToY(-1);
					break;
				}
				if (o.getY() == l.getSizeMat() - 1) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, l.getWorldNum()))
						tmpCoord = tmpL.getPosWorld(l.getWorldNum());
					else
						tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
					tmpCoord.addToY(1);
					if (!checkMovePossible(tmpCoord, d, tmpL)) {
						break;
					}
					else
						return checkMovePossible(tmpCoord, d, tmpL);
				}
				if (tmpMat[o.getX()][o.getY() + 1] == Cells.VIDE) {
					return true;
				}	
			}

			//while (tmpMat[o.getX()][o.getY()] != Cells.VIDE && tmpMat[o.getX()][o.getY()] != Cells.JOUEUR) {
			while (!o.equals(tmpCoord2)) {
				if (l.isAWorld(o.getX(), o.getY())) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					if (checkMovePossible(new CoordSet(tmpL.getSizeMat() / 2, 0), d, tmpL)) {
						worldAcces[tmpMat[o.getX()][o.getY()]]++;
						return checkMovePossible(new CoordSet(tmpL.getSizeMat() / 2, 0), d, tmpL);
					}
					else {
						o.addToY(-1);
					}
				}
				else {
					o.addToY(-1);
				}

				if (o.getY() == -1)
					break;
				
			}
			return false;
		}

		return true;
	}

	public int getNumMove(CoordSet o, Direction d, LevelMove l, int n) {
		int [][] m;
		LevelMove next;
		CoordSet tmpCoord;

		if (d == Direction.HAUT) {
			m = l.getLevelData();

			if (m[o.getX()][o.getY()] == Cells.VIDE || m[o.getX()][o.getY()] == Cells.MUR) {
				return n;
			}
/*
			if (o.getX() == 0) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToX(-1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}*/
			if (l.isAWorld(o.getX(), o.getY()) && worldAcces[m[o.getX()][o.getY()]] >= 1) {
				next = univ.get(m[o.getX()][o.getY()]);
				return getNumMove(new CoordSet(next.getSizeMat() - 1, next.getSizeMat() / 2), d, next, n);
			}
			else if (o.getX() == 0) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToX(-1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}
			else {
				o.addToX(-1);
				n++;
				return getNumMove(o, d, l, n);
			}
		}

		if (d == Direction.BAS) {
			m = l.getLevelData();

			if (m[o.getX()][o.getY()] == Cells.VIDE || m[o.getX()][o.getY()] == Cells.MUR) {
				return n;
			}

	/*		if (o.getX() == l.getSizeMat() - 1) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToX(1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}*/
			if (l.isAWorld(o.getX(), o.getY()) && worldAcces[m[o.getX()][o.getY()]] >= 1) {
				next = univ.get(m[o.getX()][o.getY()]);
				return getNumMove(new CoordSet(0, next.getSizeMat() / 2), d, next, n);
			}
			else if (o.getX() == l.getSizeMat() - 1) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToX(1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}
			else {
				o.addToX(1);
				n++;
				return getNumMove(o, d, l, n);
			}
		}

		if (d == Direction.GAUCHE) {
			m = l.getLevelData();

			if (m[o.getX()][o.getY()] == Cells.VIDE || m[o.getX()][o.getY()] == Cells.MUR) {
				return n;
			}

		/*	if (o.getY() == 0) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToY(-1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}*/
			if (l.isAWorld(o.getX(), o.getY()) && worldAcces[m[o.getX()][o.getY()]] >= 1) {
				next = univ.get(m[o.getX()][o.getY()]);
				return getNumMove(new CoordSet(next.getSizeMat() / 2, next.getSizeMat() - 1), d, next, n);
			}
			else if (o.getY() == 0) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToY(-1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}
			else {
				o.addToY(-1);
				n++;
				return getNumMove(o, d, l, n);
			}
		}

		if (d == Direction.DROITE) {
			m = l.getLevelData();

			if (m[o.getX()][o.getY()] == Cells.VIDE || m[o.getX()][o.getY()] == Cells.MUR) {
				return n;
			}

	/*		if (o.getY() == l.getSizeMat() - 1) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToY(1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}*/
			if (l.isAWorld(o.getX(), o.getY()) && worldAcces[m[o.getX()][o.getY()]] >= 1) {
				next = univ.get(m[o.getX()][o.getY()]);
				return getNumMove(new CoordSet(next.getSizeMat() / 2, 0), d, next, n);
			}
			else if (o.getY() == l.getSizeMat() - 1) {
				next = univ.get(l.getOutsideWorld());
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				tmpCoord.addToY(1);
				n++;
				return getNumMove(tmpCoord, d, next, n);
			}
			else {
				o.addToY(1);
				n++;
				return getNumMove(o, d, l, n);
			}
		}

		return n;
	}

	public void move(CoordSet o, Direction d, LevelMove l, int nMove) {
		if (d == Direction.HAUT) {
			int n = nMove;
			LevelMove tmpL = l, tmpL2 = l;
			int [][] tmpMat = l.getLevelData();
			int tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2;
			tmpMat[o.getX()][o.getY()] = Cells.VIDE;
			
			while (n > 0) {
				if (o.getX() == 0) {
					tmpL = univ.get(tmpL2.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getX() - 1, tmpL.getPosWorld(tmpL2.getWorldNum()).getY());
					else
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX() - 1, tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY());
				/*	if (n < nMove) {
						tmpMat[o.getX()][o.getY()] = tmpCell1;
					}*/
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						o.chgCoord(tmpL.getPosWorld(tmpL2.getWorldNum()).getX() - 1, tmpL.getPosWorld(tmpL2.getWorldNum()).getY());
					else
						o.chgCoord(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX() - 1, tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY());
					tmpMat = tmpL.getLevelData();
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
					tmpL2 = tmpL;
					n--;
					continue;
				}
				else
					tmpCell2 = tmpMat[o.getX() - 1][o.getY()];

				if (tmpL.isAWorld(o.getX() - 1, o.getY()) && worldAcces[tmpMat[o.getX() - 1][o.getY()]] >= 1) {
					tmpL = univ.get(tmpMat[o.getX() - 1][o.getY()]);
					tmpCell2 = univ.get(tmpMat[o.getX() - 1][o.getY()]).getLevelData(tmpL.getSizeMat() - 1, tmpL.getSizeMat()/2);
					tmpMat = tmpL.getLevelData();
					o.chgCoord(tmpL.getSizeMat() - 1, tmpL.getSizeMat()/2);
					if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
						tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat() - 1, tmpL.getSizeMat()/2);
						tmpMat = tmpL.getLevelData();
						while (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
							univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
							tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
							tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat() - 1, tmpL.getSizeMat()/2);
							tmpMat = tmpL.getLevelData();
						}
						tmpMat[o.getX()][o.getY()] = tmpCell1;
						tmpCell1 = tmpCell2;
						n--;
						continue;
					}
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
				}
				else if (tmpMat[o.getX() - 1][o.getY()] != Cells.MUR) {
					tmpMat[o.getX() - 1][o.getY()] = tmpCell1;
					tmpCell1 = tmpCell2;
					o.addToX(-1);
					if (o.getX() == 0) {
						n--;
						continue;
					}
					tmpCell2 = tmpMat[o.getX() - 1][o.getY()];
				}
				n--;
			}
		}

		if (d == Direction.BAS) {
			int n = nMove;
			LevelMove tmpL = l, tmpL2 = l;
			int [][] tmpMat = l.getLevelData();
			int tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2;
			tmpMat[o.getX()][o.getY()] = Cells.VIDE;
			
			while (n > 0) {
				if (o.getX() == tmpL2.getSizeMat() - 1) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getX() + 1, tmpL.getPosWorld(tmpL2.getWorldNum()).getY());
					else
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX() + 1, tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY());
					/*
					if (n < nMove) {
						tmpMat[o.getX()][o.getY()] = tmpCell1;
					}*/
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						o.chgCoord(tmpL.getPosWorld(tmpL2.getWorldNum()).getX() + 1, tmpL.getPosWorld(tmpL2.getWorldNum()).getY());
					else
						o.chgCoord(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX() + 1, tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY());
					tmpMat = tmpL.getLevelData();
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
					tmpL2 = tmpL;
					n--;
					continue;
				}
				else
					tmpCell2 = tmpMat[o.getX() + 1][o.getY()];	

				if (tmpL.isAWorld(o.getX() + 1, o.getY()) && worldAcces[tmpMat[o.getX() + 1][o.getY()]] >= 1) {
					tmpL = univ.get(tmpMat[o.getX() + 1][o.getY()]);
					tmpCell2 = univ.get(tmpMat[o.getX() + 1][o.getY()]).getLevelData(0, tmpL.getSizeMat()/2);
					tmpMat = tmpL.getLevelData();
					o.chgCoord(0, tmpL.getSizeMat()/2);
					if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
						tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(0, tmpL.getSizeMat()/2);
						tmpMat = tmpL.getLevelData();
						while (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
							univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
							tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
							tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(0, tmpL.getSizeMat()/2);
							tmpMat = tmpL.getLevelData();
						}
						tmpMat[o.getX()][o.getY()] = tmpCell1;
						tmpCell1 = tmpCell2;
						n--;
						continue;
					}
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
				}
				else if (tmpMat[o.getX() + 1][o.getY()] != Cells.MUR) {
					tmpMat[o.getX() + 1][o.getY()] = tmpCell1;
					tmpCell1 = tmpCell2;
					o.addToX(1);
					if (o.getX() == l.getSizeMat() - 1) {
						n--;
						continue;
					}
					tmpCell2 = tmpMat[o.getX() + 1][o.getY()];
				}
				n--;
			}
		}

		if (d == Direction.GAUCHE) {
			int n = nMove;
			LevelMove tmpL = l, tmpL2 = l;
			int [][] tmpMat = l.getLevelData();
			int tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2;
			tmpMat[o.getX()][o.getY()] = Cells.VIDE;
			
			while (n > 0) {
				if (o.getY() == 0) {
					tmpL = univ.get(tmpL2.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getX(), tmpL.getPosWorld(tmpL2.getWorldNum()).getY() - 1);
					else
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX(), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY() - 1);
				/*	
					if (n < nMove) {
						tmpMat[o.getX()][o.getY()] = tmpCell1;
					}
				*/	
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						o.chgCoord(tmpL.getPosWorld(tmpL2.getWorldNum()).getX(), tmpL.getPosWorld(tmpL2.getWorldNum()).getY() - 1);
					else
						o.chgCoord(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX(), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY() - 1);

					tmpMat = tmpL.getLevelData();
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
					tmpL2 = tmpL;
					n--;
					continue;
				}
				else
					tmpCell2 = tmpMat[o.getX()][o.getY() - 1];	
				

				if (tmpL.isAWorld(o.getX(), o.getY() - 1) && worldAcces[tmpMat[o.getX()][o.getY() - 1]] >= 1) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY() - 1]);
					tmpCell2 = univ.get(tmpMat[o.getX()][o.getY() - 1]).getLevelData(tmpL.getSizeMat()/2, tmpL.getSizeMat() - 1);
					tmpMat = tmpL.getLevelData();
					o.chgCoord(tmpL.getSizeMat()/2, tmpL.getSizeMat() - 1);
					if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
						tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat()/2, tmpL.getSizeMat() - 1);
						tmpMat = tmpL.getLevelData();
						while (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
							univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
							tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
							tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat()/2, tmpL.getSizeMat() - 1);
							tmpMat = tmpL.getLevelData();
						}
						tmpMat[o.getX()][o.getY()] = tmpCell1;
						tmpCell1 = tmpCell2;
						n--;
						continue;
					}
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
				}
				else if (tmpMat[o.getX()][o.getY() - 1] != Cells.MUR) {
					tmpMat[o.getX()][o.getY() - 1] = tmpCell1;
					tmpCell1 = tmpCell2;
					o.addToY(-1);
					if (o.getY() == 0) {
						n--;
						continue;
					}
					tmpCell2 = tmpMat[o.getX()][o.getY() - 1];
				}
				n--;
			}
		}

		if (d == Direction.DROITE) {
			int n = nMove;
			LevelMove tmpL = l, tmpL2 = l;
			int [][] tmpMat = l.getLevelData();
			int tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2;
			tmpMat[o.getX()][o.getY()] = Cells.VIDE;
			
			while (n > 0) {
				if (o.getY() == tmpL2.getSizeMat() - 1) {
					tmpL = univ.get(tmpL2.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getX(), tmpL.getPosWorld(tmpL2.getWorldNum()).getY() + 1);
					else
						tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX(), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY() + 1);
					/*
					if (n < nMove) {
						tmpMat[o.getX()][o.getY()] = tmpCell1;
					}*/
					if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
						o.chgCoord(tmpL.getPosWorld(tmpL2.getWorldNum()).getX(), tmpL.getPosWorld(tmpL2.getWorldNum()).getY() + 1);
					else
						o.chgCoord(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getX(), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getY() + 1);
					tmpMat = tmpL.getLevelData();
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
					tmpL2 = tmpL;
					n--;
					continue;
				}
				else
					tmpCell2 = tmpMat[o.getX()][o.getY() + 1];	

				if (tmpL.isAWorld(o.getX(), o.getY() + 1) && worldAcces[tmpMat[o.getX()][o.getY() + 1]] >= 1) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY() + 1]);
					tmpCell2 = univ.get(tmpMat[o.getX()][o.getY() + 1]).getLevelData(tmpL.getSizeMat()/2, 0);
					tmpMat = tmpL.getLevelData();
					o.chgCoord(tmpL.getSizeMat()/2, 0);
					if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
						tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat()/2, 0);
						tmpMat = tmpL.getLevelData();
						while (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
							univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(l.getWorldNum());
							tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
							tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.getSizeMat()/2, 0);
							tmpMat = tmpL.getLevelData();
						}
						tmpMat[o.getX()][o.getY()] = tmpCell1;
						tmpCell1 = tmpCell2;
						n--;
						continue;
					}
					tmpMat[o.getX()][o.getY()] = tmpCell1;
					if (tmpL.isAWorld(o.getX(), o.getY())) {
						univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
					}
					tmpCell1 = tmpCell2;
				}
				else if (tmpMat[o.getX()][o.getY() + 1] != Cells.MUR) {
					tmpMat[o.getX()][o.getY() + 1] = tmpCell1;
					tmpCell1 = tmpCell2;
					o.addToY(1);
					if (o.getY() == l.getSizeMat() - 1) {
						n--;
						continue;
					}
					tmpCell2 = tmpMat[o.getX()][o.getY() + 1];
				}
				n--;
			}
		}
	}

	public boolean addLevel(LevelMove level) {
		this.taille++;
		return this.univ.add(level);
	}

	public boolean winConditionMetUniv() {
		int i, k;
	//	boolean player = false;
	//	boolean box = true;
/*
		for (i = 0; i < getTaille(); i++) {
			if (univ.get(i).playerSpawn().equals(univ.get(i).getPlayerTarget())) {
				player = true;
			}
		}
*/
		for (i = 0; i < getTaille(); i++) {
			LevelMove tmp = univ.get(i);
			int [][] tmpMat = tmp.getLevelData();
			for (k = 0; k < tmp.getListTarget().size(); k++) { 
				//if (tmpMat[tmp.getListTarget().get(k).getX()][tmp.getListTarget().get(k).getY()] != Cells.BOITE && !tmp.isAWorld(tmp.getListTarget().get(k).getX(), tmp.getListTarget().get(k).getY()))
    			if (tmpMat[tmp.getListTarget().get(k).getX()][tmp.getListTarget().get(k).getY()] == Cells.VIDE)
    				return false;
    		}
    	}
/*
    	if (player && box)
    		return true;
    	else 
    		return false;
   */
    	return true;
	}

	public int getPlayerSpawnWorld() {
		int k, i, j;
		int [][] tmp;

		for (k = 0; k < getTaille(); k++) {
			tmp = univ.get(k).getLevelData();
			for (i = 0; i < univ.get(k).getSizeMat(); i++) {
				for (j = 0; j < univ.get(k).getSizeMat(); j++) {
					if (tmp[i][j] == Cells.JOUEUR) {
						return k;
					}
				}
			}
		}
		return -1;
	}

	public void initWorldAcces() {
		int i;
		
		worldAcces = new int[this.getTaille()];
		for (i = 0; i < this.getTaille(); i++) {
			worldAcces[i] = 0;
		}
	}

	public void resetWorldAcces() {
		int i;
		
		for (i = 0; i < this.getTaille(); i++) {
			worldAcces[i] = 0;
		}
	}

	public void printWorldAcces() {
		int i;
		
		for (i = 0; i < this.getTaille(); i++) {
			System.out.println(i+": "+worldAcces[i]);
		}
	}

    public int whereWorldIs(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++) {
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld) {
    					return l.getWorldNum();
    				}
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld)) {
    					return tmpMat[i][j];
    				}
    			}
    		}
    	}
    	return -5;
    }

    public boolean isWorldIn(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++) {
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld) {
    					return true;
    				}
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld)) {
    					return isWorldIn(univ.get(tmpMat[i][j]), numWorld);
    				}
    			}
    		}
    	}
    	return false;
    }

    public boolean isWorldInThisLevel(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++) {
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (tmpMat[i][j] == numWorld) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

	public ArrayList<LevelMove> getUnivers() {
		return this.univ;
	}

	public int getTaille() {
		return this.taille;
	}
}