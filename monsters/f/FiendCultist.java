/**
 * 
 */
package com.dndcombat.monsters.f;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.warlock.HellishRebuke;
import com.dndcombat.spells.wizard.Fireball;
import com.dndcombat.spells.wizard.ScorchingRay;

/**
 
 */
public class FiendCultist extends Player {
 
	private static final long serialVersionUID = 1L;

	public FiendCultist() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Fiend Cultist");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Cultist.webp");

	    str(19).dex(15).con(16).inte(12).wis(18).cha(10);

		// Saving Throws
		strSave(+4).dexSave(+2).conSave(+6).intSave(+1).wisSave(+7).chaSave(+0);
	
		 // Combat Stats
		ac(16).init(+5).hp(127).speed(30).cr(8);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
	    
	    setMonsterSpellDC(15);
	    setMonsterSpellAttack(7);

	/*
	
	- saving throws ending on the targets next turn have a duration of 0
	- saving throws triggered on hit can be added to the action (duration customized)
	- save or damage/effect is WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), damage, save, 0); see gladiator. 1/2 dmg on save is not the default
	
	- legendary action type and cost need to be set if you have legendary actions
	- legendary action Within spell needs to be set on the action for some reason
	
	- healing or buff spells must be marked at the action level to be helping
	
	*/
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(PactAxe(),PactAxe(),PactAxe() ));

        addAction(new Action("Pact Axe", RefList.actiontypesmainaction)
                .addAttack(PactAxe()));
        
        getCreature().addModifierType(new HellishRebuke());
        
        addAction(fireball());
        addAction(ScorchingRay());
	}
    
 	private Action fireball() {

		Action a = new Action("Fireball", RefList.actiontypesmainaction);

		a.setMaxNbrUsesPerBattle(2);
		a.setMonsterCastAtSlot(6);
		a.setMonsterSpell(new Fireball());

		return a;

	}

 	private Action ScorchingRay() {

		Action a = new Action("Scorching Ray", RefList.actiontypesmainaction);

		a.setMonsterCastAtSlot(5);
		a.setMonsterSpell(new ScorchingRay());

		return a;

	}


	private Attack PactAxe() {
	
        Attack a = new Attack("Pact Axe"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(12) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        a.addDamage().nbrDice(3).dieType(8).damageTypeRefId(RefList.damagetypesfire);
        
        return a;
    }

}

