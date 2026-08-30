import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { ContactStackParamList } from '@/navigation/types.ts';
import ContactListScreen from '@/screens/Contact/ContactList/ContactListScreen.tsx';
import ContactRequestListScreen from '@/screens/Contact/ContactRequestList/ContactRequestListScreen.tsx';

const Stack = createNativeStackNavigator<ContactStackParamList>();

function ContactNavigator() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ContactList" component={ContactListScreen} />
      <Stack.Screen
        name="ContactRequestList"
        component={ContactRequestListScreen}
      />
    </Stack.Navigator>
  );
}

export default ContactNavigator;
