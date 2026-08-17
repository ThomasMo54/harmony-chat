import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { COLORS } from "../constants/colors.ts";
import LoginScreen from "../screens/Auth/Login/LoginScreen.tsx";

const Tab = createBottomTabNavigator();

function TabNavigator() {
  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: COLORS.primary,
      }}
    >
      <Tab.Screen name="Home" component={LoginScreen} />
    </Tab.Navigator>
  );
}

export default TabNavigator;