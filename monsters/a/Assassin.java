/**
 * 
 */
package com.dndcombat.monsters.a;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Assassin extends Player {
 
	private static final long serialVersionUID = 1L;

	public Assassin() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Assassin");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Assassin.webp");

        // Ability Scores
        str(11).dex(18).con(14).inte(16).wis(11).cha(10);

        // Saving Throws
        strSave(0).dexSave(7).conSave(2).intSave(6).wisSave(0).chaSave(0);

        // Combat Stats
        ac(16).init(10).hp(97).speed(30).cr(8);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
	    
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Shortsword(),Shortsword(),Shortsword()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(LightCrossbow(),LightCrossbow(),LightCrossbow()));
        
        // Actions
        addAction(new Action("Shortsword", RefList.actiontypesmainaction)
                .addAttack(Shortsword()));
        addAction(new Action("Light Crossbow", RefList.actiontypesmainaction)
                .addAttack(LightCrossbow()));
        
        //setMonsterSpellDC(13);
        //addAction(LightningBolt());
        // delay moving closer
        //setMoveCloserAfterRound(2);
    }
    

	private Attack Shortsword() {
		
		DurationType poison = new DurationType(10);
		poison.whenToEnd = WhenToEnd.StartOfTurn;
        Attack a = new Attack("Shortsword");
        a.setAttackBonus(7)
         .setMeleereach(5)
         .setRollToHitInd(1)
         //.setGrappleOnHit(true)
         //.setGrappleDc(13)
        // .setGrappleLargeOrSmaller(true)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(4)
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage()
        .nbrDice(5)
        .dieType(6)
        .damageTypeRefId(RefList.damagetypespoison)
        .autoApplyCondition(RefList.conditionspoisoned, poison)
        ;
        
        return a;
    }
	

	private Attack LightCrossbow() {
        Attack a = new Attack("Light Crossbow");
        a.setAttackBonus(7)
        .setAttackRange(80)
        .setLongRange(320)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(8)
         .bonus(4)
         .damageTypeRefId(RefList.damagetypespiercing);
        a.addDamage()
        .nbrDice(6)
        .dieType(6)
        .damageTypeRefId(RefList.damagetypespoison);
        
        return a;
    }
	
	 
}
