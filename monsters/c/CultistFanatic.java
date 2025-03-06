/**
 * 
 */
package com.dndcombat.monsters.c;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.cleric.SpiritualWeapon;

/**
 
 */
public class CultistFanatic extends Player {
 
	private static final long serialVersionUID = 1L;

	public CultistFanatic() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Cultist Fanatic");
	    setPlayersSide(false);
 
	    getCreature().setImageUrl("/assets/monsters/MM/Cult Fanatic.webp");

	    str(11).dex(14).con(12).inte(10).wis(14).cha(13);

		// Saving Throws
		strSave(+0).dexSave(+2).conSave(+1).intSave(+0).wisSave(+4).chaSave(+1);
	
		 // Combat Stats
		ac(13).init(+2).hp(44).speed(30).cr(2);
		
		setMonsterSpellDC(14);
		setMonsterSpellAttack(4);
 
        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

        addAction(new Action("Pact Blade", RefList.actiontypesmainaction)
                .addAttack(PactBlade()));

        addAction(new Action("Spiritual Weapon", RefList.actiontypesbonusaction).setLimitedUses(2)
                .addAttack(SpiritualWeapon()));

        addAction(new Action("Hold Person", RefList.actiontypesmainaction).setLimitedUses(1)
                .addAttack(HoldPerson()));
        
        getCreature().addModifierType(new DefaultModifierType("Strategies") {
 
			private static final long serialVersionUID = 1L;
        	@Override
        	public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
        		if (action.is("Spiritual Weapon") && state.getRound() == 1) {
        			throw new InvalidActionException();
        		}
        		return 0;
        	}
        });
	}


	private Attack HoldPerson() {
		Attack a = new Attack("Hold Person");
		a.setSpell(new com.dndcombat.spells.cleric.HoldPerson());
		return a;
	}

	private Attack SpiritualWeapon() {
		Attack a = new Attack("Spiritual Weapon");
		a.setSpell(new SpiritualWeapon());
		return a;
	}

	private Attack PactBlade() {
	
        Attack a = new Attack("Pact Blade"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

