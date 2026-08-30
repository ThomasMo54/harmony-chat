import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useTranslation } from 'react-i18next';
import { COLORS } from '@/constants/colors.ts';
import AppIcon from '@/components/AppIcon/AppIcon.tsx';
import ContactNavigator from '@/navigation/ContactNavigator.tsx';
import ProfileScreen from '@/screens/Profile/ProfileScreen.tsx';

const Tab = createBottomTabNavigator();

function TabNavigator() {
  const { t } = useTranslation();

  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: COLORS.primary,
      }}
    >
      <Tab.Screen
        name="ContactList"
        component={ContactNavigator}
        options={{
          title: t('tabs.contactList'),
          tabBarIcon: ({ color, size }) => (
            <AppIcon name="list" size={size - 4} color={color} />
          ),
        }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={{
          title: t('tabs.profile'),
          tabBarIcon: ({ color, size }) => (
            <AppIcon name="user" size={size - 4} color={color} />
          ),
        }}
      />
    </Tab.Navigator>
  );
}

export default TabNavigator;
