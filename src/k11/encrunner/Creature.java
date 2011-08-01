package k11.encrunner;

public class Creature {
	public Integer id;
	public String name;
	public Integer enc_hp;
	public Integer position;
	public Integer character_id; //TODO implement PlayerCharacter class properly
	public Integer monster_id; //TODO implement NonPlayerCharacter class properly
	
	/** Initializes a Creature to null values (can be either a PC or NPC) */
	Creature()
	{
		id=0;
		name="";
		enc_hp=0;
		position=0;
	}
	
	/** Initializes a creature with argument values */
	Creature(Integer id_arg,String name_arg,Integer enc_hp_arg,Integer position_arg)
	{
		id=id_arg;
		name=name_arg;
		enc_hp=enc_hp_arg;
		position=position_arg;
	}
}
