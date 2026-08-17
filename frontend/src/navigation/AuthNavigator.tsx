import { createNativeStackNavigator } from '@react-navigation/native-stack';
import LoginScreen from "../screens/Auth/Login/LoginScreen.tsx";
import RegisterScreen from "../screens/Auth/Register/RegisterScreen.tsx";
import { AuthStackParamList } from "./types.ts";
import VerifyScreen from "../screens/Auth/Verify/VerifyScreen.tsx";

const Stack = createNativeStackNavigator<AuthStackParamList>();

function AuthNavigator() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen name="Register" component={RegisterScreen} />
      <Stack.Screen name="Verify" component={VerifyScreen} />
    </Stack.Navigator>
  );
}

export default AuthNavigator;